package lab2.v3;

import java.util.Random;

/**
 * <p>
 * 3. Klasikinis "skaitytojų - rašytojų" veikos sinchronizatorius
 * kai skaitančiųjų "skaitytojų" skaičius negali viršyti N - programos
 * parametras (pastaba - nepainioti su gamintojo-vartotojo problema !).
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        int maxReaders = 5;
        if (args.length > 0) {
            maxReaders = Integer.parseInt(args[0]);
        }

        System.out.println("Max readers: " + maxReaders);

        ReadWriteSync fileSync = new ReadWriteSync(maxReaders);

        for (int i = 1; i < maxReaders; i++) {
            new Reader(fileSync, "Skaitytojas-" + i).start();
        }
        for (int i = 1; i <= 1; i++) {
            new Writer(fileSync, "Rašytojas-" + i).start();
        }
    }

    static class Reader extends Thread {
        private final ReadWriteSync readWriteSync;
        private static final Random RANDOM = new Random();

        public Reader(ReadWriteSync readWriteSync, String readerName) {
            super(readerName);
            this.readWriteSync = readWriteSync;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    sleep(RANDOM.nextInt(1000) + 100);
                    readWriteSync.startRead(getName());
                    sleep(RANDOM.nextInt(1000) + 100);
                    readWriteSync.endRead(getName());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Writer extends Thread {
        private final ReadWriteSync fileSync;
        private static final Random RANDOM = new Random();

        public Writer(ReadWriteSync fileSync, String writerName) {
            super(writerName);
            this.fileSync = fileSync;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    fileSync.startWrite(getName());
                    sleep(RANDOM.nextInt(1000) + 100);
                    fileSync.endWrite(getName());
                    sleep(RANDOM.nextInt(1000) + 100);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class ReadWriteSync {
        private final int maxReaders;
        private int fileContent = 0; // simuliuoja bendrą failą
        private int readers = 0;
        private boolean writerActive = false;

        public ReadWriteSync(int maxReaders) {
            this.maxReaders = maxReaders;
        }

        public synchronized void startWrite(String writerName) {
            if (writerActive || readers > 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                    return;
                }
            }
            writerActive = true;
            System.out.println(writerName + " pradėjo rašyti");
        }

        public synchronized void endWrite(String writerName) {
            //if (!writerActive) {
            //    return;
            //}

            fileContent++;
            writerActive = false;
            System.out.println(writerName + " baigė rašyti. Failo turinys: " + fileContent);
            notifyAll();
        }

        public synchronized void startRead(String readerName) {
            if (writerActive || readers >= maxReaders) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                    return;
                }
            }
            readers++;
            System.out.println(readerName + " pradėjo skaityti (skaitytojų " + readers + "). Failo turinys: " + fileContent);
        }

        public synchronized void endRead(String readerName) {
            readers--;
            System.out.println(readerName + " baigė skaityti (skaitytojų " + readers + "). Failo turinys: " + fileContent);
            notifyAll();
        }
    }
}
