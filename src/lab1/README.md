## 1 Laboratorinis

### LT
https://klevas.mif.vu.lt/~rvck/lygis/pratybos/uzduot1.html

Užduoties formulavimas bendras visiems kurso dalyviams. Tikslas suvokti kritinės sekcijos problemą, sugebėti iššaukti negatyvius padarinius, ir išspręsti KS problemą konkrečiam modeliniam taikymui.

Priemonės: Java arba WIN32/POSIX ar pan. gijų posistemės.

Programos tipas - konsolinė programa. Apimtis: ~30-50 kodo eilučių.

<ins>Užduoties formulavimas.</ins>
Sugalvoti probleminę situaciją, kurioje keli procesai naudoja resursą/resursus. Programa, imituojanti šių procesų darbą, turi demonstruoti (pirmasis darbo režimas), jog nesinchronizuota keletos gijų manipuliacija resursu (pavyzdžiui, kelioms gijoms prieinamu kintamuoju, kurio reikšmę gijos nuskaito/atnaujima) gali iššaukti nekorektišką programos darbo rezultatą.

Kitas, t.y., sinchronizuotas programos veikimo režimas (valdomas komandinės eilutės parametru bei naudojantis synchronized Java (C#) metodus arba semaforus/mutex'sus C,C++ aplinkose) turi užtikrinti teisingą programos veikimą.

Programos komentare trumpai aprašyti dalykinę sritį bei problemą. Vizualiai (komentarais) išskirti kode kritines sekcijas. Siekiant padidinti programos nedeterminizmą leidžiama įtraukti atsitiktinius gijų vykdymo užlaikymus (Thread.sleep(...)), tačiau, tik ištyrus, jog be užlaikymų programa nepakliūna į nepageidaujamas būsenas.

<ins>Problemos pavyzdys:</ins> (nenaudoti atsiskaitant).

Bendras resursas - dvi sąskaitos. Keli procesai vykdo pervedimus iš vienos sąskaitos į kitą. Nekorektiškas programos darbo rezultatas - pakitęs suminis pinigų likutis sąskaitose.

1 pastaba. Nebus laikoma tinkama tokia programa, kuri iliustruoja gijų vykdymo nedeterminizmą, bet negalime nurodyti koks deterministinis vyksmas yra teisingas. Pavyzdžiui, netiks "PingPong" (žr. žemiau) pavidalo sprendimas. 2 pastaba. Nerašyti sudėtingų programų, kuriose procesai bendrauja tarpusavyje, naudodami įvykių mechanizmą (wait(), notify()).

[Pavyzdys.java](./pavyzdys/Main.java) - užduoties sprendimo java šablonas (matematikams). PS studentams - nenaudoti!

Jums gali praversti pirmojo užsiėmimo programos. PingPong programėlė, kuri demonstruoja gijų paleidimą , SUN'o gijų vedlys ar priminimas iš Java paskaitos apie gijas.

### EN
https://emokymai.vu.lt/mod/assign/view.php?id=82042

The solution of data race problem is to be presented as single *.java or other source file or .zip archive. Please provide name of the author and short description of the problem. Graded up to 0.5 points.

The aim is to understand the problem of the critical section, to present the simulation of the "real world" computation that suffers from data race condition, and to correct the solution using using using synchronization statements/locks or other synchr. constructions .

Tools: Java, C++, C#,  Python or other programming languages or WIN32 / POSIX or similar synchronization libraries .

Program intended to be a console program. Scope: ~ 30-50 lines of code.

Briefly describe the subject area and problem in the source file. Visually  highlight critical sections in the source code. In order to increase the non deterministic behaviour of the program, it is allowed to include random thread execution delays (Thread.sleep (...)), but only after careful examination that without delays the program does not fall into undesirable states.

Example of a problem: (do not use for  assignment purpose).

There are two accounts. Several processes transfers some amoun of money from first account to second and in opposite direction. If at the end the total balance is different than initial - that proves the incorrect result of the program.

<ins>Notes.</ins>
* A program that illustrates the nondeterminism of thread execution will not be considered appropriate, if we cannot specify which deterministic result is being correct.
* Do not write complex programs in which processes communicate with each other using the event mechanism (wait (), notify ()).

The circumstances below must be disclosed:
* which program output is considered correct?
* which execution scenario causes an invalid output?
* where (in the code) is/are the Critical Section/-s?
* can Critical Section be divided into smaller sections or can contain fewer lines of code?
