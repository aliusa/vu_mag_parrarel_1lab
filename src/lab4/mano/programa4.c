#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/* qsort comparator */
static int compare_ints(const void *a, const void *b) {
    int x = *(const int*)a;
    int y = *(const int*)b;
    return (x > y) - (x < y);
}

/* merge two sorted arrays into a newly allocated array */
static int* merge_two(const int *a, int na, const int *b, int nb) {
    int *out = (int*)malloc((size_t)(na + nb) * sizeof(int));
    if (!out) return NULL;

    int i = 0, j = 0, k = 0;
    while (i < na && j < nb) {
        out[k++] = (a[i] <= b[j]) ? a[i++] : b[j++];
    }
    while (i < na) out[k++] = a[i++];
    while (j < nb) out[k++] = b[j++];
    return out;
}

static int is_sorted(const int *arr, int n) {
    for (int i = 1; i < n; i++) if (arr[i-1] > arr[i]) return 0;
    return 1;
}

int main(int argc, char **argv) {
    MPI_Init(&argc, &argv);

    int rank = 0, np = 1;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &np);

    if (argc != 4) {
        if (rank == 0) {
            fprintf(stderr, "Usage: %s <np> <array_size> <debug|fast>\n", argv[0]);
            fprintf(stderr, "Example: mpirun -np 64 %s 33554432 fast\n", argv[0]);
        }
        MPI_Finalize();
        return 1;
    }

    np = atoi(argv[1]);
    const int N = atoi(argv[2]);
    const int debug = (strcmp(argv[3], "debug") == 0);

    if (N <= 0) {
        if (rank == 0) fprintf(stderr, "Error: array_size must be > 0\n");
        MPI_Finalize();
        return 1;
    }

    /* We keep it simple: require N divisible by np (like your original). */
    if (N % np != 0) {
        if (rank == 0) {
            fprintf(stderr, "Error: array_size (%d) must be divisible by processes (%d)\n", N, np);
        }
        MPI_Finalize();
        return 1;
    }

    const int local_n0 = N / np;

    int *global = NULL;
    int *local  = (int*)malloc((size_t)local_n0 * sizeof(int));
    if (!local) {
        fprintf(stderr, "[rank %d] malloc local failed\n", rank);
        MPI_Abort(MPI_COMM_WORLD, 2);
    }

    if (rank == 0) {
        global = (int*)malloc((size_t)N * sizeof(int));
        if (!global) {
            fprintf(stderr, "[rank 0] malloc global failed\n");
            MPI_Abort(MPI_COMM_WORLD, 2);
        }
        srand((unsigned)time(NULL));
        for (int i = 0; i < N; i++) global[i] = rand() % 100000;
    }

    MPI_Barrier(MPI_COMM_WORLD);
    const double t0 = MPI_Wtime();

    /* 1) Scatter chunks */
    MPI_Scatter(global, local_n0, MPI_INT,
                local,  local_n0, MPI_INT,
                0, MPI_COMM_WORLD);

    /* global no longer needed for speed measurements (free early) */
    if (rank == 0) {
        free(global);
        global = NULL;
    }

    /* 2) Local sort */
    qsort(local, (size_t)local_n0, sizeof(int), compare_ints);

    /* 3) Tree-based merge:
       - ranks with (rank % (2*step) == 0) receive from rank+step and merge
       - ranks with (rank % (2*step) != 0) send to rank-step and exit
    */
    int *data = local;
    int  data_n = local_n0;

    for (int step = 1; step < np; step *= 2) {

        if ((rank % (2 * step)) == 0) {
            int partner = rank + step;
            if (partner < np) {
                /* receive partner's size, then data */
                int recv_n = 0;
                MPI_Recv(&recv_n, 1, MPI_INT, partner, 100, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                int *recv_buf = (int*)malloc((size_t)recv_n * sizeof(int));
                if (!recv_buf) {
                    fprintf(stderr, "[rank %d] malloc recv_buf failed (recv_n=%d)\n", rank, recv_n);
                    MPI_Abort(MPI_COMM_WORLD, 3);
                }

                MPI_Recv(recv_buf, recv_n, MPI_INT, partner, 101, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                /* merge locally */
                int *merged = merge_two(data, data_n, recv_buf, recv_n);
                if (!merged) {
                    fprintf(stderr, "[rank %d] malloc merged failed\n", rank);
                    MPI_Abort(MPI_COMM_WORLD, 4);
                }

                free(recv_buf);
                free(data);

                data = merged;
                data_n += recv_n;
            }
        } else {
            int partner = rank - step;
            /* send size then data, then break out (this rank is done) */
            MPI_Send(&data_n, 1, MPI_INT, partner, 100, MPI_COMM_WORLD);
            MPI_Send(data, data_n, MPI_INT, partner, 101, MPI_COMM_WORLD);
            free(data);
            data = NULL;
            break;
        }
    }

    MPI_Barrier(MPI_COMM_WORLD);
    const double t1 = MPI_Wtime();

    if (rank == 0) {
        const double ms = (t1 - t0) * 1000.0;
        printf("MPI tree-merge sort time: %.2f ms (processes=%d, N=%d)\n", ms, np, N);
        if (debug) {
            printf("Sorted check: %s\n", is_sorted(data, data_n) ? "OK" : "FAILED");
        }
        free(data);
    }

    MPI_Finalize();
    return 0;
}
