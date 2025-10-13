package lab1;

import java.util.Random;

/**
 * Tema: Data Race problema - koncertų bilietų pirkimo sistema.
 * <p>
 * Aprašymas:
 * Keli pirkėjai (gijos) tuo pačiu metu perka bilietus iš sistemos.
 * Be sinchronizacijos gali įvykti "data race" - parduodama daugiau bilietų, nei turime.
 * <p>
 * Teisingas rezultatas: visi bilietai turi būti parduodami, bet ne daugiau, nei egzistuoja.
 * Neteisingas scenarijus: išspausdinama, kad bilietų parduota daugiau, nei buvo.
 */
public class Main {
    public static void main(String[] args) {
        TicketCounter ticketCounter = new TicketCounter(10);

        Runnable buyerTask = () -> {
            String name = Thread.currentThread().getName();
            while (ticketCounter.sellTicket(name)) {
                try {
                    Thread.sleep((int) (Math.random() * 100));
                } catch (InterruptedException e) {
                    //
                }
            }
        };

        //Sukuriame kelias gijas (pirkėjus)
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(buyerTask, "Pirkėjas-" + i);
            thread.start();
        }
    }
}

class TicketCounter {
    private int tickets;//Pradinis bilietų kiekis
    private static final Random RANDOM = new Random();

    public TicketCounter(int tickets) {
        this.tickets = tickets;
    }

    public boolean sellTicket(String buyerName) {
        int buyCount = 1;
        try {
            //KRITINĖ VIETA. be "synchronized" kelios gijos (threads (pirkėjai)) tuo pat metu pakeičia "tickets" reikšmę
            //synchronized (this) {
                if (tickets > 0) {
                    //Prailginam laiką, kad geriau parodyt "data race" problemą, kad kuri nors gija nespėtų pakeist "tickets" reikšmės
                    Thread.sleep(RANDOM.nextInt(101)+50);//50 - min, 100 - max

                    tickets -= buyCount;

                    System.out.println(buyerName + " nuspirko " + buyCount + " bilietą(-us). Liko " + tickets + " vnt.");
                    return true;
                }
                throw new Exception(buyerName + " bandė pirkti bilietus, bet neužteko!");
            //}
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
