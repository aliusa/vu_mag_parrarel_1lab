package lab3v3_v2;

import java.util.*;

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
public class ParallelMergeSort {
    private static int nThreads;
    private static boolean debug;
    private static int[] A;     // current array
    private static int[] B;     // auxiliary array (same size)
    private static volatile int size;

    // Coordinator state for each pass
    private static class PassCoordinator {
        // width (block size)
        volatile int width;
        // number of merge tasks this pass
        volatile int nTasks;
        // next task index to give to a worker
        int nextTask;
        // sync helpers
        int completedWorkers;
        boolean passStarted;

        public PassCoordinator() {
            width = 1;
            nTasks = 0;
            nextTask = 0;
            completedWorkers = 0;
            passStarted = false;
        }

        // call under synchronized(this)
        public void initPass(int w) {
            width = w;
            nextTask = 0;
            completedWorkers = 0;
            // number of merges = ceil(size / (2*w))
            nTasks = (size + 2*w - 1) / (2*w);
            passStarted = true;
            notifyAll();
        }

        // return next task index or -1 if none left
        public synchronized int getNextTask() {
            if (!passStarted) return -1;
            if (nextTask >= nTasks) return -1;
            return nextTask++;
        }

        // worker calls when done with pass
        public synchronized void workerDone() {
            completedWorkers++;
            if (completedWorkers >= nThreads) {
                passStarted = false;
                notifyAll();
            }
        }

        // wait until pass is started
        public synchronized void awaitStart() {
            while (!passStarted) {
                try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
            }
        }

        // wait until pass finished (master)
        public synchronized void awaitFinish() {
            while (passStarted) {
                try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
            }
        }
    }

    private static final PassCoordinator coord = new PassCoordinator();

    // Worker thread: persistent
    private static class Worker extends Thread {
        public Worker(int id) { super("Worker-" + id); }

        public void run() {
            while (true) {
                // wait for pass to start
                coord.awaitStart();
                // If pass not started (possibly termination), check passStarted
                synchronized (coord) {
                    if (!coord.passStarted) {
                        // Either termination or spurious - continue loop to check
                        continue;
                    }
                }

                while (true) {
                    int taskIndex;
                    synchronized (coord) {
                        taskIndex = coord.getNextTask();
                    }
                    if (taskIndex == -1) break; // no tasks left this pass

                    // Each task corresponds to merging two runs:
                    int w = coord.width;
                    int left = taskIndex * 2 * w;
                    int mid = Math.min(left + w - 1, size - 1);
                    int right = Math.min(left + 2 * w - 1, size - 1);

                    if (left > right || left >= size) continue; // nothing to do

                    // Perform merge from A -> B (note we swap A/B in master after pass)
                    merge(A, B, left, mid, right);

                    if (debug) {
                        System.out.printf("[%s] Sulieta [%d..%d] ir [%d..%d]%n",
                                getName(), left, mid, mid + 1, right);
                        try { Thread.sleep(5); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                }

                // signal worker done for this pass
                coord.workerDone();

                // If master has signaled termination (width == 0), break loop and exit
                if (coord.width == 0) break;
            }
        }
    }

    // merge from src->dst for interval left..right where mid splits
    private static void merge(int[] src, int[] dst, int left, int mid, int right) {
        if (mid >= right) {
            // Right part empty: copy left part
            for (int i = left; i <= right; i++) dst[i] = src[i];
            return;
        }
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (src[i] <= src[j]) dst[k++] = src[i++];
            else dst[k++] = src[j++];
        }
        while (i <= mid) dst[k++] = src[i++];
        while (j <= right) dst[k++] = src[j++];
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Naudojimas: java ParallelMergeSortIterative <nThreads> <size> <debug|fast>");
            return;
        }
        nThreads = Integer.parseInt(args[0]);
        size = Integer.parseInt(args[1]);
        debug = args[2].equalsIgnoreCase("debug");

        if (nThreads < 1) nThreads = 1;
        if (size < 1) size = 1;

        A = new int[size];
        B = new int[size];
        Random rnd = new Random(42);
        for (int i = 0; i < size; i++) A[i] = rnd.nextInt(1000000);

        if (debug) {
            System.out.println("Pradinis masyvas: " + Arrays.toString(Arrays.copyOf(A, Math.min(size, 64))) + (size > 64 ? " ..." : ""));
        }

        // create nThreads persistent workers
        Thread[] workers = new Thread[nThreads];
        for (int i = 0; i < nThreads; i++) {
            workers[i] = new Worker(i);
            workers[i].start();
        }

        long t0 = System.nanoTime();

        // iterative bottom-up passes
        int width = 1;
        boolean fromAtoB = true;
        while (width < size) {
            // initialize pass
            synchronized (coord) {
                coord.initPass(width);
            }
            // workers will pick tasks and merge from A->B if fromAtoB true
            // but our merge uses arrays A,B directly; ensure we pass right src/dst references
            // we have workers merging using current A->B references

            // Wait until all workers finish this pass
            coord.awaitFinish();

            // swap arrays references (A<->B)
            int[] tmp = A; A = B; B = tmp;
            fromAtoB = !fromAtoB;
            width *= 2;
        }

        long t1 = System.nanoTime();

        // signal workers termination by setting width=0 and notifying them
        synchronized (coord) {
            coord.width = 0;
            coord.passStarted = true; // wake up any waiting workers so they can exit
            coord.notifyAll();
        }

        // join workers
        for (Thread w : workers) w.join();

        // after swaps, final sorted array is in A
        if (debug) {
            System.out.println("Surikiuotas masyvas: " + Arrays.toString(Arrays.copyOf(A, Math.min(size, 64))) + (size > 64 ? " ..." : ""));
        }

        double seconds = (t1 - t0) / 1_000_000_000.0;
        System.out.printf("Vykdymo laikas: %.6f s%n", seconds);

        // quick correctness check
        if (!isSorted(A)) {
            System.err.println("KLAIDA: masyvas nėra surikiuotas!");
        }
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) if (arr[i - 1] > arr[i]) return false;
        return true;
    }
}
