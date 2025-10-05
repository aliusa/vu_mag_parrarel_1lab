package lab2.pvz;

/*
 * Main2.java
 * @author R.Vaicekauskas
 * Antrosios užduoties sprendimo java šablonas
 * Demonstruoja gijos veiklos sinchronizaciją, naudojant java objektą
 * Šiame pavyzdyje kelios gijos įjungia/išjungia jungtuką
 *   - įjungti galima tik jeigu jungtukas išjungtas
 *   - išjungti galima tik jeigu jungtukas įjungtas
 */

// Irašyti reikiamą paketo vardą
//package antroji;

/**
 * Dirbant NetBeans aplinkoje klasė "Main" sugeneruojama automatiškai
 */
public class Main2 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Programa pradeda darba");
        TestThread.pradeti();
        System.out.println("Programa baigia darba.");
    }
}

/*
   Naujai sukurta klase.
   Gijos klasė, turi būti išvesta iš Thread
*/
class TestThread extends Thread
{
    // Gijos objekto specifiniai duomenys
    SinchronizacijosObjektas bendras;

    // Konstruktorius, skirtas perduoti duomenis gijos objektui
    public TestThread(SinchronizacijosObjektas bendras)
    {
        this.bendras = bendras;
    }

    // Metodas, vykdomas paleidus giją
    // Thread.run()
    public void run()
    {
        System.out.println("Gija " + this + " paleista");

        // Demonstracijai keletą kartų kreipiamės į sinchronizacinį
        // atnaujinimo konflikto galimybę
        for (int i = 0; i < 5; i++)
        {
            System.out.println("Gija " + this + ": pries ijungiant jungikli");

            // Kviečiame sinchronizuotą metodą, kuris gali blokuotis
            bendras.turnOn();

            System.out.println("Gija " + this + ":      jungiklis ijungtas");

            try  { Thread.sleep(10);}
            catch (InterruptedException exc) {
                System.out.println("Ivyko klaida "+exc);
            }

            System.out.println("Gija " + this + ":      pries isjungiant jungikli");

            // Kviečiame sinchronizuotą metodą, kuris (potencialiai) gali blokuotis
            bendras.turnOff();

            System.out.println("Gija " + this + ": jungiklis isjungtas");

        } // End for
        System.out.println("Gija " + this + " baigia darba");
    }

    // Metodas paleidžiantis gijas darbui ir išvedantis rezultatą
    public static void pradeti()
    {
        // Sukuriame objektą, kurį bendrai naudos kelios gijos
        SinchronizacijosObjektas bendras = new SinchronizacijosObjektas();

        try
        {
            // Sukuriame ir startuojame gijas, kurių darbą sinchronizuos bendras objektas

            // Sukuriame ir startuojame pirmąją giją
            // perduodami kaip parametrą objektą "bendras"
            Thread t1 = new TestThread(bendras);
            t1.start();

            // Sukuriame ir startuojame kitą giją
            Thread t2 = new TestThread(bendras);
            t2.start();

            // Laukiame, kol abi gijos baigs darbą
            t1.join();  t2.join();

            System.out.println("main() baige darba.");
        }
        catch (InterruptedException exc)
        {
            System.out.println("Ivyko klaida "+exc);
        }
    }
}

/*
   Klasė, aprašanti bendrai gijų naudojamą sinchronizacijos objektą
   Konkrečiam taikymui turi būti pakeista

   Demonstruoja: jungiklį, kurį įjungti ir išjungti gali tik tam tikro tipo gija
*/

class SinchronizacijosObjektas
{
    // Objekto būsena
    // (Jungtukas išjungtas)
    boolean isOn=false;

    // Metodas, kuri potencialiai gali blokuotis
    synchronized void turnOn()
    {
        // (Jeigu metodo negalime vykdyti) laukiame
        while(isOn)
        {
            try
            {
                wait();
            }
            catch(InterruptedException exc)
            {
                System.out.println("Nutrauktas gijos darbas: "+exc);
                System.exit(4);
            }
        }

        isOn = true; // Atnaujiname būseną

        notifyAll(); // (Jeigu reikia) pažadiname laukiančius
    }

    synchronized void turnOff()
    {
        while(!isOn) // (Jeigu metodo negalime vykdyti) laukiame
        {
            try
            {
                wait();
            }
            catch(InterruptedException exc)
            {
                System.out.println("Nutrauktas gijos darbas: "+exc);
                System.exit(4); // Užbaigti programą
            }
        }

        isOn = false; // Atnaujiname būseną

        notifyAll(); // (Jeigu reikia) pažadiname laukiančius
    }
}
