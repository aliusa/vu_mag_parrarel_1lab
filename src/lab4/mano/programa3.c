#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h>

// Pagalbinė funkcija, skirta qsort palyginimui
int compare_ints(const void *a, const void *b) {
    int arg1 = *(const int *)a;
    int arg2 = *(const int *)b;

    if (arg1 < arg2) return -1;
    if (arg1 > arg2) return 1;
    return 0;
}

// Funkcija dviejų surikiuotų masyvų sujungimui (Merge)
void merge_arrays(int *arr1, int size1, int *arr2, int size2, int *result) {
    int i = 0, j = 0, k = 0;
    while (i < size1 && j < size2) {
        if (arr1[i] <= arr2[j]) {
            result[k++] = arr1[i++];
        } else {
            result[k++] = arr2[j++];
        }
    }
    while (i < size1) result[k++] = arr1[i++];
    while (j < size2) result[k++] = arr2[j++];
}

// Funkcija, skirta patikrinti, ar masyvas yra surikiuotas
int is_sorted(int *array, int size) {
    for (int i = 0; i < size - 1; i++) {
        if (array[i] > array[i + 1]) {
            return 0; // Nesurikiuotas
        }
    }
    return 1; // Surikiuotas
}

// Spausdinti masyvo dalį (jei dydis nėra per didelis)
void print_array_part(int *array, int size, int max_print, const char *label) {
    int print_size = size < max_print ? size : max_print;
    printf("%s: ", label);
    for (int i = 0; i < print_size; i++) {
        printf("%d ", array[i]);
    }
    if (size > max_print) {
        printf("...");
    }
    printf("\n");
}

int main(int argc, char **argv) {
    int rank, np;
    int array_size;
    int avg_iterations = 1; // Sumažiname iteracijų skaičių dėl sudėtingesnio MPI kodo
    int debug = 0;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &np);

    if (argc != 4) {
        if (rank == 0) {
            fprintf(stderr, "Neteisingas argumentų skaičius. Naudojimas: %s <np> <dydis> <režimas>\n", argv[0]);
        }
        MPI_Finalize();
        return 1;
    }

    // Argumentų nuskaitymas (np imamas iš MPI_Comm_size, bet mes naudojame argumentus dydžiui ir režimui)
    array_size = atoi(argv[2]);
    if (strcmp(argv[3], "debug") == 0) {
        debug = 1;
    }
    // Masyvo dydis turi būti dalus iš procesų skaičiaus, supaprastinimui
    if (array_size % np != 0) {
        if (rank == 0) {
            fprintf(stderr, "Klaida: Masyvo dydis (%d) turi būti dalus iš procesų skaičiaus (%d).\n", array_size, np);
        }
        MPI_Finalize();
        return 1;
    }
    int elements_per_proc = array_size / np;
    int *global_array = NULL;
    int *local_array = (int *)malloc(elements_per_proc * sizeof(int));
    double avg_time = 0.0;

    // MPI Merge Sort atliekamas avg_iterations kartų
    for (int iavg = 0; iavg < avg_iterations; iavg++) {
        if (rank == 0) {
            // Pagrindinis procesas (P0) inicijuoja masyvą
            if (iavg == 0) { // Global_array atmintis skiriama tik pirmą kartą
                global_array = (int *)malloc(array_size * sizeof(int));
                srand(time(NULL));
                for (int i = 0; i < array_size; i++) {
                    global_array[i] = rand() % 100000;
                }
            }

            if (debug) {
                printf("%d procesai paleisti. Masyvo dydis: %d\n", np, array_size);
                print_array_part(global_array, array_size, 64, "Pradinis masyvas");
            }
        }

        double t0 = MPI_Wtime();

        // 1. Duomenų paskirstymas (Scatter)
        // Išsiunčia elements_per_proc sveikųjų skaičių iš global_array į local_array kiekvienam procesui
        MPI_Scatter(global_array, elements_per_proc, MPI_INT,
                    local_array, elements_per_proc, MPI_INT,
                    0, MPI_COMM_WORLD);

        // 2. Vietinis rikiavimas (Sort)
        qsort(local_array, elements_per_proc, sizeof(int), compare_ints);

        if (debug) {
            printf("[%s] Procesas %d: Gavo ir surikiavo %d elementų.\n",
                   "MPI", rank, elements_per_proc);
            // print_array_part(local_array, elements_per_proc, 16, "Lokaliai surikiuota dalis");
        }

        // 3. Surinkimas (Gather)
        // Surinkti visas surikiuotas dalis atgal į P0 global_array
        MPI_Gather(local_array, elements_per_proc, MPI_INT,
                   global_array, elements_per_proc, MPI_INT,
                   0, MPI_COMM_WORLD);

        // 4. Galutinis sujungimas (Merge) - atlieka tik P0
        if (rank == 0) {
            // Kadangi turime np surikiuotas dalis, reikia jas sujungti.
            // Paprasčiausias būdas yra sujungti poromis, lygiai kaip Java kode TopDownMergeSort.
            // Vietoje tikro Merge Sort implementacijos, čia atliekamas finalinis n-dalių sujungimas

            int *merged_array = (int *)malloc(array_size * sizeof(int));
            int current_size = elements_per_proc;

            // Kopijuojame pirmąją dalį kaip pradinę surikiuotą seką
            memcpy(merged_array, global_array, current_size * sizeof(int));

            // Nuoseklus surikiuotų dalių suliejimas
            for (int i = 1; i < np; i++) {
                int start_index = i * elements_per_proc;
                int *current_part = global_array + start_index; // Rikiuojama i-toji dalis

                int *temp_merged = (int *)malloc((current_size + elements_per_proc) * sizeof(int));

                merge_arrays(merged_array, current_size,
                             current_part, elements_per_proc,
                             temp_merged);

                current_size += elements_per_proc;

                // Perkeliame rezultatą atgal į merged_array tolimesniam sujungimui
                memcpy(merged_array, temp_merged, current_size * sizeof(int));
                free(temp_merged);
            }

            // Nukopijuojame galutinį surikiuotą masyvą atgal į global_array
            memcpy(global_array, merged_array, array_size * sizeof(int));
            free(merged_array);

            double t1 = MPI_Wtime();
            double elapsed_time_ms = (t1 - t0) * 1000.0;
            avg_time += elapsed_time_ms;

            if (debug) {
                print_array_part(global_array, array_size, 164, "Surikiuotas masyvas");
                if (is_sorted(global_array, array_size)) {
                    printf("Patikrinimas: Masyvas SURIKIUOTAS.\n");
                } else {
                    printf("Patikrinimas: Masyvas NESURIKIUOTAS!\n");
                }
                printf("\nLaikas: %.2f ms\n", elapsed_time_ms);
            }
            if (!debug && iavg == avg_iterations - 1) {
                printf("\navg laikas (%d ciklų): %.2f ms (%d thread/s)\n",
                       avg_iterations, avg_time / avg_iterations, np);
            }
        }
    }

    free(local_array);
    if (rank == 0 && global_array != NULL) {
        free(global_array);
    }

    MPI_Finalize();
    return 0;
}