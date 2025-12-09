/**
 * 3. Masyvo elementų rūšiavimas "suliejimo" pavidalo metodu.
 * (Galite nustatyti, kad masyvo dydis 2-ju laipsnis).
 * Gugliafrazė: "merge sort"
 *
 * <p>
 * Tema: Lygiagretus masyvo rikiavimas "merge sort" metodu.
 * </p>
 *
 * <p>
 * Aprašymas:
 * Programa demonstruoja, kaip masyvas rikiuojamas keliomis gijomis,
 * kurios veikia lygiagrečiai. Darbas paskirstomas dinamiškai –
 * kai gija baigia rikiuoti savo dalį, ji gali būti panaudota kitiems darbams.
 * </p>
 */
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>
#include <string.h>

typedef struct {
    int *arr;
    int *temp;
    int left;
    int right;
    int threads;
} merge_params;

int debug = 0;

// Forward declarations
void* topDownSplitMergeThread(void *args);
void topDownSplitMerge(int *arr, int left, int right, int threads, int *temp);
void oneThreadMergeSort(int *arr, int left, int right, int *temp);
void topDownMerge(int *arr, int left, int mid, int right, int *temp);

long currentTimeMs() {
    struct timeval time;
    gettimeofday(&time, NULL);
    return time.tv_sec * 1000 + time.tv_usec / 1000;
}

int main(int argc, char **args) {
    if (argc != 4) {
        printf("Usage: %s <threads> <size> <debug|fast>\n", args[0]);
        return 1;
    }

    int avgIterations = 25;
    int size = atoi(args[2]);
    int debugMode = strcmp(args[3], "debug") == 0;
    debug = debugMode;

    int threadsArray[] = {
        256,128,64,32,16,8,4,2
    };
    int threadsCount = sizeof(threadsArray)/sizeof(int);

    for (int j = 0; j < threadsCount; j++) {
        int nThreads = threadsArray[j];
        long avg = 0;

        for (int iter = 0; iter < avgIterations; iter++) {

            if (size < 2) size = 2;

            int *array = malloc(size * sizeof(int));
            int *temp  = malloc(size * sizeof(int));

            for (int i = 0; i < size; i++) {
                array[i] = rand() % 100000;
            }

            long t0 = currentTimeMs();
            topDownSplitMerge(array, 0, size - 1, nThreads, temp);
            long t1 = currentTimeMs();

            avg += (t1 - t0);

            free(array);
            free(temp);
        }

        printf("\navg time (%d runs): %ld ms (%d threads)\n",
               avgIterations, avg / avgIterations, nThreads);
    }

    return 0;
}

// ---------------- Parallel Merge Sort -----------------

void topDownSplitMerge(int *arr, int left, int right, int threads, int *temp) {
    if (left >= right) return;

    if (threads <= 1) {
        oneThreadMergeSort(arr, left, right, temp);
        return;
    }

    int mid = (left + right) / 2;

    pthread_t t1, t2;
    merge_params p1 = {arr, temp, left, mid, threads/2 - 1};
    merge_params p2 = {arr, temp, mid+1, right, threads/2 - 1};

    pthread_create(&t1, NULL, topDownSplitMergeThread, &p1);
    pthread_create(&t2, NULL, topDownSplitMergeThread, &p2);

    pthread_join(t1, NULL);
    pthread_join(t2, NULL);

    topDownMerge(arr, left, mid, right, temp);
}

void* topDownSplitMergeThread(void *args) {
    merge_params *p = (merge_params*) args;
    topDownSplitMerge(p->arr, p->left, p->right, p->threads, p->temp);
    return NULL;
}

// --------------- One-thread merge sort -----------------

void oneThreadMergeSort(int *arr, int left, int right, int *temp) {
    if (left >= right) return;

    int mid = (left + right) / 2;
    oneThreadMergeSort(arr, left, mid, temp);
    oneThreadMergeSort(arr, mid+1, right, temp);
    topDownMerge(arr, left, mid, right, temp);
}

// ----------------------- Merge -------------------------

void topDownMerge(int *arr, int left, int mid, int right, int *temp) {
    int i = left;
    int j = mid + 1;
    int k = left;

    while (i <= mid && j <= right) {
        temp[k++] = (arr[i] <= arr[j]) ? arr[i++] : arr[j++];
    }
    while (i <= mid) temp[k++] = arr[i++];
    while (j <= right) temp[k++] = arr[j++];

    for (i = left; i <= right; i++)
        arr[i] = temp[i];
}
