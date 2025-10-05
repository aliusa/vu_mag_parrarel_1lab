package lab3.v3;

import java.util.Arrays;
import java.util.Random;

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
 * <p>
 * Kvietimo pvz.:
 * <code>
 * java ParallelMergeSort 4 1048576 fast
 * </code>
 * <p>
 * Parametrai:
 * args[0] – gijų skaičius
 * args[1] – masyvo dydis (turi būti 2^k)
 * args[2] – režimas: "debug" arba "fast"
 */
public class ParallelMergeSortTopDown {
    private static int nThreads = 4;
    private static boolean debug = false;
    private static int[] array;
    private static int[] temp;

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Wrong number of arguments. java ParallelMergeSort <gijos> <dydis> <režimas>");
        }

        nThreads = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        debug = args[2].equalsIgnoreCase("debug");

        //Masyvo dydis turi būti >= 2 kad būtų ką
        if (size < 2) {
            size = 2;
        }

        //generate random number array
        array = new int[size];
        temp = new int[size];
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rnd.nextInt(100000);
        }

        System.out.println(nThreads + " gijos paleistos. Masyvo dydis: " + size);
        if (debug) {
            System.out.println("Pradinis masyvas: " + Arrays.toString(Arrays.copyOf(array, Math.min(size, 64))) + (Math.min(size, 64) == size ? "" : " ..."));
        }


        long t0 = System.currentTimeMillis();
        topDownSplitMerge(array, 0, array.length - 1, nThreads);
        long t1 = System.currentTimeMillis();

        if (debug)
            System.out.println("Surikiuotas masyvas: " + Arrays.toString(Arrays.copyOf(array, Math.min(size, 64))) + (Math.min(size, 64) == size ? "" : " ..."));

        if (!debug)
            System.out.println("\nLaikas: " + (t1 - t0) + " ms");
    }

    /**
     * Split int[] into 2 runs, sort both runs into B[], merge both runs from B[] to A[]
     * iBegin is inclusive; iEnd is exclusive (A[iEnd] is not in the set).
     */
    private static void topDownSplitMerge(int[] arr, int left, int right, int threads) {
        if (left >= right) return;

        //Jei gijų skaičius pasiekė 1, pereiname į įprastą Merge Sort.
        if (threads <= 1) {
            oneThreadMergeSort(arr, left, right);
            return;
        }

        //split the run longer than 1 item into halves
        int mid = (left + right) / 2;

        //recursively sort both runs from array A[] into B[]
        Thread leftThread = new Thread(() -> topDownSplitMerge(arr, left, mid, threads / 2-1));
        Thread rightThread = new Thread(() -> topDownSplitMerge(arr, mid + 1, right, threads / 2-1));

        leftThread.start();
        rightThread.start();

        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //final merge
        topDownMerge(arr, left, mid, right);
    }


    /**
     *
     */
    private static void oneThreadMergeSort(int[] arr, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        oneThreadMergeSort(arr, left, mid);
        oneThreadMergeSort(arr, mid + 1, right);
        topDownMerge(arr, left, mid, right);
    }

    /**
     * Left source half is A[ iBegin:iMiddle-1].
     * Right source half is A[iMiddle:iEnd-1   ].
     * Result is            B[ iBegin:iEnd-1   ].
     */
    private static void topDownMerge(int[] arr, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            temp[k++] = (arr[i] <= arr[j]) ? arr[i++] : arr[j++];
        }
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];

        for (i = left; i <= right; i++) {
            arr[i] = temp[i];
        }

        if (debug) {
            //System.out.printf("Sulieta [%d..%d] ir [%d..%d]%n", left, mid, mid + 1, right);

            System.out.printf("[%s] Suliejimas. Rikiuota [%d..%d] ir [%d..%d]%n",
                    Thread.currentThread().getName(), left, mid, mid + 1, right);
        }
    }
}
