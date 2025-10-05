package lab2.v0;

/**
 * Studijų knygelės paskutinis skaitmuo: 0.
 * <p>
 * "Klasikinis" įvykių skaitiklis.
 * Inicializuojamas 0. Turi funkcijas:
 * * advance() - nedalomai padidinti skaitliuką vienetu;
 * * read   () - nuskaityti skaitliuko reiksmę;
 * * await  (value) - laukti, kol skaitliuko reiksme nesusilygins su value
 * Pateikti skaitiklio prasmingą panaudojimą.
 */
public class Main {
    public static void main(String[] args) {
        EventCounter eventCounter = new EventCounter();

        Thread manager5 = new Thread(() -> eventCounter.await(5), "Vadybininkas[5]");
        Thread manager10 = new Thread(() -> eventCounter.await(10), "Vadybininkas[10]");
        Thread manager15 = new Thread(() -> eventCounter.await(15), "Vadybininkas[15]");

        manager5.start();
        manager10.start();
        manager15.start();

        //Bilietus nuperka po vieną

        for (int i = 0; i < 17; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            eventCounter.advance();
        }
        System.out.println("\n[PABAIGA] Visi bilietai parduoti!");
    }
}

class EventCounter {
    private int soldTickets = 0;

    public synchronized void await(int targetTickets) {
        while (read() < targetTickets) {
            System.out.println(Thread.currentThread().getName() + " laukia kol bus parduota " + targetTickets + " bilietų. Dabar: " + read());
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getName() + " praneša: Pasiekta " + targetTickets + " bilietų.");
    }

    public synchronized void advance() {
        soldTickets++;
        System.out.println("\n[KASA] Parduotas bilietas nr #" + read());
        notifyAll();
    }

    public synchronized int read() {
        return soldTickets;
    }
}
