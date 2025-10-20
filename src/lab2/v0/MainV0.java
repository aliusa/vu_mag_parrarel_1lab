package lab2.v0;

/**
 * <p>
 * "Klasikinis" įvykių skaitiklis.
 * Inicializuojamas 0. Turi funkcijas:
 * * advance() - nedalomai padidinti skaitliuką vienetu;
 * * read   () - nuskaityti skaitliuko reiksmę;
 * * await  (value) - laukti, kol skaitliuko reiksme nesusilygins su value
 * Pateikti skaitiklio prasmingą panaudojimą.
 */
public class MainV0 {
    public static void main(String[] args) {
        int startTicketCount = 12;
        EventCounter eventCounter = new EventCounter();

        Manager manager10 = new Manager(eventCounter, 10, "Vadybininkas[10]");
        Thread manager5Thread = new Thread(manager10);

        manager5Thread.start();

        //Bilietus nuperka po vieną
        for (int i = 1; i <= startTicketCount; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            eventCounter.advance();
        }
        System.out.println("\n=== PABAIGA. Visi bilietai parduoti! ===");


        //Laukiame, kol visi vadybininkai pabaigs savo darbą
        try {
            manager5Thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //Tikrinimas — ar suma teisinga
        System.out.println("\n=== ATASKAITA ===");
        System.out.println("Pagrindinis skaitiklis: " + eventCounter.read());
        System.out.println(manager10.getName() + " suskaičiavo: " + manager10.getMySoldTickets());
        if (eventCounter.read() == manager10.getMySoldTickets()) {
            System.out.println((char)27 + "[92m" + "SUCCESS");
        } else {
            System.out.println((char)27 + "[91m" + "FAILED");
        }
    }
}

class EventCounter {
    private int soldTickets = 0;

    public synchronized void advance() {
        soldTickets++;
        System.out.println("[KASA] Parduotas bilietas #" + soldTickets);
        notifyAll(); //Pažadina laukiančius vadybininkus
    }

    /**
     * Laukia, kol pasiekiama konkreti reikšmė
     */
    public synchronized void await(int targetTickets) {
        while (soldTickets < targetTickets) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized int read() {
        return soldTickets;
    }
}
/**
 * Vadybininkas, kuris seka pagrindinį skaitiklį ir kaupia savo bilietų kiekį.
 */
class Manager implements Runnable {
    private final EventCounter eventCounter;
    private final int targetTickets;
    private final String name;
    private int mySoldTickets = 0;

    public Manager(EventCounter eventCounter, int targetTickets, String name) {
        this.eventCounter = eventCounter;
        this.targetTickets = targetTickets;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            eventCounter.await(mySoldTickets + 1); //Laukia kol atsiras naujas bilietas
            mySoldTickets++;

            System.out.println(name + " atnaujina: mano skaitiklis = " + mySoldTickets);

            if (mySoldTickets >= targetTickets) {
                System.out.println(name + " praneša: pasiekta " + targetTickets + " bilietų riba! Baigiu darbą.");
                break;
            }
        }
    }

    public int getMySoldTickets() {
        return mySoldTickets;
    }

    public String getName() {
        return name;
    }
}
