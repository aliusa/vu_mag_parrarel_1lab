#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>

int debug = 0;

/* ---------- Utility ---------- */

long currentTimeMs() {
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec * 1000 + t.tv_usec / 1000;
}

/* ---------- Merge Sort ---------- */

void merge(int *arr, int *temp, int left, int mid, int right) {
    int i = left, j = mid + 1, k = left;

    while (i <= mid && j <= right)
        temp[k++] = (arr[i] <= arr[j]) ? arr[i++] : arr[j++];

    while (i <= mid) temp[k++] = arr[i++];
    while (j <= right) temp[k++] = arr[j++];

    for (i = left; i <= right; i++)
        arr[i] = temp[i];
}

void mergeSort(int *arr, int *temp, int left, int right) {
    if (left >= right) return;
    int mid = (left + right) / 2;
    mergeSort(arr, temp, left, mid);
    mergeSort(arr, temp, mid + 1, right);
    merge(arr, temp, left, mid, right);
}

/* ---------- Main ---------- */

int main(int argc, char **argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (argc != 4) {
        printf("Ne 3 argumentai! Usage: %s <threads> <array_size> <debug|fast>\n", argv[0]);
        if (rank == 0)
            printf("rank=0\n");
        MPI_Finalize();
        return 1;
    }

    int N = atoi(argv[1]);
    debug = strcmp(argv[2], "debug") == 0;

    if (N % size != 0) {
        if (rank == 0)
            printf("Array size must be divisible by number of processes\n");
        MPI_Finalize();
        return 1;
    }

    int local_n = N / size;

    int *global_array = NULL;
    int *local_array = malloc(local_n * sizeof(int));
    int *temp = malloc(local_n * sizeof(int));

    if (rank == 0) {
        global_array = malloc(N * sizeof(int));
        srand(0);
        for (int i = 0; i < N; i++)
            global_array[i] = rand() % 100000;
    }

    MPI_Barrier(MPI_COMM_WORLD);
    long t0 = currentTimeMs();

    /* ---------- Scatter ---------- */
    MPI_Scatter(
        global_array,
        local_n,
        MPI_INT,
        local_array,
        local_n,
        MPI_INT,
        0,
        MPI_COMM_WORLD
    );

    /* ---------- Local sort ---------- */
    mergeSort(local_array, temp, 0, local_n - 1);

    /* ---------- Tree-based merge ---------- */
    int step = 1;
    while (step < size) {
        if (rank % (2 * step) == 0) {
            if (rank + step < size) {
                int recv_n = local_n;
                int *recv_buf = malloc(recv_n * sizeof(int));

                MPI_Recv(recv_buf, recv_n, MPI_INT,
                         rank + step, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                int *merged = malloc(2 * local_n * sizeof(int));
                int i = 0, j = 0, k = 0;

                while (i < local_n && j < recv_n)
                    merged[k++] = (local_array[i] <= recv_buf[j])
                                  ? local_array[i++] : recv_buf[j++];

                while (i < local_n) merged[k++] = local_array[i++];
                while (j < recv_n) merged[k++] = recv_buf[j++];

                free(local_array);
                free(recv_buf);

                local_array = merged;
                local_n *= 2;
            }
        } else {
            int target = rank - step;
            MPI_Send(local_array, local_n, MPI_INT,
                     target, 0, MPI_COMM_WORLD);
            break;
        }
        step *= 2;
    }

    MPI_Barrier(MPI_COMM_WORLD);
    long t1 = currentTimeMs();

    if (rank == 0) {
        printf("MPI Merge Sort time: %ld ms (processes=%d)\n",
               t1 - t0, size);
    }

    MPI_Finalize();
    return 0;
}
