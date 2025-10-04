# Lygiagretūs ir išskirstyti skaičiavimai

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

Pavyzdys.java - užduoties sprendimo java šablonas (matematikams). PS studentams - nenaudoti!

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

## 2 Laboratorinis
https://klevas.mif.vu.lt/~rvck/lygis/pratybos/uzduot2.html

Realizuoti paskirtojo varianto gijų sinchronizacijos primityvą.

Varianto numeris apskaičiuojamas pagal formulę: PS:[Studijų knyg. numerio paskutinis skaitmuo]. (Pvz., numerį 616015 atitinkantis variantas yra 5.)
Matematikai: [Studijų knyg. numerio paskutinis skaitmuo] mod 5, (pvz., numerį 616017 atitinkantis variantas yra 2.)
Įmanomos ir kitos užduotis - suderinti su dėstytoju.

Instrumentas - Java aplinka (įmanomos ir kitos, pvz., C# - suderinti su dėstytoju).

Detaliau: viena klasė realizuoja sinchr. primityvą, kita/os jį panaudoja - demonstruoja teisingą veikimą (sugalvoti kokią nors modelinę situaciją). Įvykio laukimas realizuojamas Object.wait(), informavimas apie įvykį Object.notify/all() kreipiniais. Jau egzistuojančių (standartinių) sinchronizacinių klasių panaudojimas neleistinas (pvz., util.concurrent).

Main2.java - užduoties sprendimo java šablonas (matematikams). PS studentams - nenaudoti!

Programa veikia konsoliniu režimu. Į konsolę išvedami esminiai įvykiai.

### VARIANTAI

<ins>0. "Klasikinis" įvykių skaitiklis.</ins>  
   Inicializuojamas 0. Turi funkcijas
   advance() - nedalomai padidinti skaitliuką vienetu;
   read   () - nuskaityti skaitliuko reiksmę;
   await  (value) - laukti, kol skaitliuko reiksme nesusilygins
   su value.
   Pateikti skaitiklio prasmingą panaudojimą.

<ins>1.Semaforas kaip resursų skaitliukas.</ins>  
Inicializuojamas turimų resursų skaičiumi.
Operacijos:
request()         - laukia, kol resursas atsilaisvins,
release()         - grąžinamas resursas,
numberAvailable() - grąžinamas laisvų resursų skaičius
(nesiblokuojant).

<ins>2.Barjeras.</ins>  
Objektas inicializuojamas "sąveikoje" dalyvaujančių gijų skaiciumi (N).
Pagrindinė operacija - waitBarier(). Kiekviena šį metodą iškvietusi
giją laukia, kol kvietėjų skaičius taps lygus N. Tada visos N gijos
"paleidžiamos" o barjeras reinicializuojamas - paruošiamas sekančiam
waitBarier() kvietiniui.

<ins>3. Klasikinis "skaitytojų - rašytojų" veikos sinchronizatorius</ins>  
kai skaitančiųjų "skaitytojų" skaičius negali viršyti N - programos  
parametras (pastaba - nepainioti su gamintojo-vartotojo problema !).

<ins>4. "Baltųjų - Raudonųjų" gijų veikos sinchronizatorius.</ins> -  
Vienu metu leidžiama būti "aktyvioms" tik vienos spalvos gijoms  
(klasikinės  "skaitytojų-rašytojų" problemos variacija).

<ins>5. "Rytų - Vakarų" gijų veikos sinchronizatorius.</ins> -  
klasikinės  "skaitytojų-rašytojų" problemos variacija.  
Vienu metu leidžiama būti "aktyvioms" tik vienos krypties gijoms,  
bet ne daugiau negu N - programos parametras. Palyginimui situacija -  
vienos eiles automobiliu tiltas, kuriuo mašinos gali važiuoti  
viena kryptimi.

<ins>6. Duomenų perdavimo kanalas tarp dviejų gijų realizuojamas kaip žiedinis buferis.</ins>  
Duomenis - sveikieji skaičiai. Buferio ilgis - parametras. Pademonstruoti,  
kaip įėjimo duomenys, kurie nuskaitomi kaip įėjimo failo baitų reikšmės  
perduodami kitai gijai, kuri surašo duomenis į išėjimo failą.

<ins>7. Multipleksinis žiedinis buferis kaip pranešimo perdavimo kanalas</ins>    
tarp vienos gijos - siuntėjo, bei N (parametras) gijų gavėjų.  
Kiekvienas pranešimas laikomas nuskaitytu, jeigu jį nuskaitė VISOS gijos - gavėjos.  
Gija, vykdanti skaitymo veiksmą nurodo savo numerį iš intervalo 0..N-1.

<ins>8. Masyvo "užraktas"  turi operacijas</ins>  
lock(int indexFrom, in indexTo),  
unlock(int indexFrom, in indexTo).  
Metodas lock(), užrakina masyvo elementus nuo indexFrom iki indexTo imtinai.  
Jeigu bent vienas iš elementų jau "užrakintas" kitos gijos, kreipinys lock()  
blokuojasi. unlock - atlaisvina užrakintus elementus (su tais pačiais indeksais).  
Realizuoti užraktą bei prasmingai panaudoti (pavyzdžiui, rušiuojant masyvo elementus)

<ins>9. Barjeras - su duomenų apsikeitimu</ins>  
Objektas inicializuojamas "sąveikoje" dalyvaujančių gijų skaiciumi (N).  
Pagrindinė operacija -  
int  waitBarier(int reikšmė).  
Kiekviena šį metodą iškvietusi giją laukia, kol kvietėjų skaičius taps lygus N.  
Tada visos N gijos "paleidžiamos" - metodas gražina asociatyvios operacijos (pvz. sumos)  
su visomis "reikšmėmis" rezultatą - o barjeras reinicializuojamas, t.y., paruošiamas  
sekančiam waitBarier() kvietiniui.

## 3 Laboratorinis
https://klevas.mif.vu.lt/~rvck/lygis/pratybos/uzduot3.html

### Formulavimas
Suprojektuoti pateiktajai užduočiai (problemai) **efektyvų** lygiagretaus vykdymo algoritmą ir jį realizuoti gijomis (JAVA/POSIX/C#/OpenMP/Cilk ar kitas - suderinti su dėstytoju).

"Darbinių" gijų skaicius - programos parametras. Jos "paleidžiamos" vykdymui programos pradžioje ir funkcionuoja iki pat programos pabaigos (kol užduotyje specifikuotas darbas nebus atliktas).

Sąveika tarp atskirų gijų, aišku, turi būti tokia, kad jos būtų maksimaliai "apkrautos". Be to **NELEISTINA** tokia "darbo" vykdymo disciplina, kai "darbo" dalys paskirstomos atskiriems "procesoriams" - gijoms iš anksto ir nekinta programos vykdymo eigoje. (Kitaip tariant, neleistinas sinchronizacijos tarp atskirų gijų nebuvimas - konkretaus uždavinio sprendimą šiuo aspektu suderinti su dėstytoju).

Paruošti programos vykdymą dviem režimais - *derinimo režimu*, kuriame programos vykdymas gali būti dirbtinai sulėtintas (sleep) ir į ekraną išvedamas programos vykdymo protokolas, paaiškinantis veikimo principus, bei "*sparčiuoju*" režimu, kuriame neturi būti jokių dirbtinių stabdymų, o programa išvestų į ekraną vykdymo trukmę.
Komandinė eilutė turi turėti papildomus parametrus - darbinių gijų skaičių bei "apkrovos" parametrą, nusakantį darbo apimtį (pvz., tikslumas, masyvo dydis ir t.t. ).
Sparčiuoju režimu programa turi demonstruoti vykdymo spartinimą, vykdant ją daugiaprocesorinėje sistemoje.

Sistemoje, kurioje tiriama programa turi būti bent 4 (realūs) procesoriai. MIF aplinkoje programa turi būti vykdoma MIF išskirstytų skaičiavimų klasteryje.

**Dėmesio**. Vykdant bendrosios atminties skaičiavimus klasteryje svarbu rezervuoti sistemą turinčią reikiamą procesorių skaičių vienoje stotyje. Tai galima atlikti komanda (8 branduoliai):

```shell
srun --pty -N1 -c8 $SHELL
```

Eksperimentiškai nustatyti algoritmo spartinimo, plečiamumo (scaling) bei vykd. laiko priklausomybės nuo "darbų" dydžio (grain size) charakteristikas, vykdant programą daugiaprocesorinėje aplinkoje bei pavaizduoti **grafikais**. Sugebėti pagrįsti gautuosius rezultatus. Atsiskaitant, pateikti (motyvuotą) sprendimo **aprašą**.

**Pastaba**. Rašydami (ypač) java programas, stenkitės išvengti pakartotinio objektų sukūrimo-atlaisvinimo, kadangi "šiukšlių surinkėjas" gali iškraipyti laikines programos vykdymo charakteristikas.

Žemiau pateikta programa leidžia patikrinti java programos vykdymo spartinimą paskirties sistemoje: TTest.java, TTest.class (atnaujinta 2013-12-08).


### Variantai
Variantas **[studijų knygelės nr. paskutinis skaitmuo] arba gretimas (jeigu įdomesnis)**  
Taip pat leidžiama spręsti savo pasirinktą (sunkesnį(*) / įdomesnį) variantą/uždavinį - suderinti su dėstytoju. Galima premija iki 0.5 balo.

Pastaba: nors užduoties algoritmas formuluojamas nuoseklaus algoritmo terminais, tačiau lygiagretusis algoritmas gali skirtis: svarbu išsaugoti esminę nusakyto algoritmo idėją.

<ins>1. Masyvo elementų rūšiavimas "quick sort" metodu.</ins>

<ins>2. Masyvo elementų rūšiavimas "burbuliuko" pavidalo metodu.</ins>  
T.y., metodo esmė - dviejų gretimų elementų sukeitimas vietomis.  
Gugliafrazė: "odd even sort"

<ins>3. Masyvo elementų rūšiavimas "suliejimo" pavidalo metodu.</ins>  
(Galite nustatyti, kad masyvo dydis 2-ju laipsnis).  
Gugliafrazė: "merge sort"

<ins>4. Pirminių skaičių generavimas "Eratosthenes" rėčio metodu.</ins>  
(Pirmasis pirminis skaicius 2. Išbraukiami visi kartotiniai  
žinomam pirminiui skaičiui; tai kartojma su sekančiu neišbrauktu  
pirminiu ir t.t.) Likę neišbraukti skaičiai - pirminiai.  
Gugliafrazė: "Eratosthenes prime sieve"

<ins>5. Adaptyvusis integralo skaičiavimas.</ins>  
Duota tolydi neneigiama funkcija f(X) ir du taškai a,b.  
Rasti integralą  I[a,b] f(x) dx, aproksimuojant jį plotu figuros,  
aprėžtos ašimi x, kreive f(x) bei tiesėmis y = a, y = b.  
Sprendimo būdas - dalyti sritį [a,b] į daugelį mažesnių  
sričių vertikaliomis linijomis ir sumuoti pastarųjų plotus,  
kol 'nesukonverguos' (žr. konspektus).  
Gugliafrazė: "Adaptive Quadrature"

<ins>6. Šilumos pasiskirstymo lygties sprendimas.</ins>  
Duotas pradinis temperatūrų pasiskirstymo masyvas.  
Atlikti pastarojo iteratyvią transformaciją  
V(i, j) = (V(i+1, j) + V(i, j+1) + V(i-1, j) + V(i, j-1)) / 4  
kol "nenusistovės" (kraštinių piksel'ių reikšmės fiksuotos).  
Gugliafrazės: Laplace's heat equation  Jacobi iteration

<ins>7. Ilgiausios Collatz'o iteracijos paieška</ins> duotajame skaičių intervale.  
Kiekvienam i = 1,2,3,...  
A(0) - pradinis iteracijos elementas, natūrinis.  
A(i+1) = 3 * A(i) + 1, kai A(i) - nelyginis  
A(i+1) = A(i) / 2,     kai A(i) - lyginis.

Pagal Collatz'o hipotezę, kiekvienam natūriniam n, po baigtinio
skaičiaus žingsnių galima gauti 1.

Pvz.: 7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1.

Taigi, skaičių [a,b] intervale reikia rasti skaičių n, kuris iteruoja
į 1-ą "ilgiausiai".

Pastabos: skaičiams naudokite long tipą. Atkreipkite dėmesį, jog iteracijų skaičius labai skiriasi!

Gugliafrazė: "Collatz problem"

<ins>8. Mandelbrot'o aibės skaičiavimas</ins>  
Pageidautina rezultatą suformuoti kaip paveikslėlį (galima naudoti jau parašytą kodą).  
Gugliafrazė: "Mandelbrot set"

<ins>9. N Valdovių išdėstymo NxN lentoje uždavinys.</ins>  
Gugliafrazė: "N-queen"

<ins>10. Automato "Conway's Life Game" simuliacija.</ins>  
Pageidautina rezultatą pavaizduoti vizualiai.

<ins>\*11. Ilgiausio dviejų simbolių eilučių posekio radimas.</ins>  
Gugliafrazė: "Longest common subsequence".

<ins>\*12. Susijusių sričių paveikslėlyje identifikavimas</ins>  
Susijusioje srityje visi pikselio kaimynai (dešinė, kairė, viršus, apačia) turi tą pačią pikselio vertę. Pradžioje visi pikseliai sunumeruoti nuo 1 iki N*N (kitame masyve). Darbo pabaigoje susijusios srities pikseliai turi įgauti didžiausią reikšmę.  
Gugliafrazė:  "connected component labeling".

<ins>\*13. Lipschitz'o funkcijos maksimumo intervale radimas, naudojant šakų ir ribų algoritmą.</ins>  
Gugliafrazės: branch bound, Lipschitz Function

<ins>\*14. N-kūnų simuliacijos atvejis</ins>  
Atstumu l < 1, kūnų trauka tenkina Niutono dėsnį ir yra proporcinga m1*m2/r^2, o kai l >=1 kūnus veikia "baltoji jėga", kurios dydis proporcingas m1 * m2 * r (t.y., toldami kūnai ima vėl labiau traukti vienas kitą!)  
Gugliafrazės: N-body problem

## 4 Laboratorinis
https://klevas.mif.vu.lt/~rvck/lygis/pratybos/uzduot4.html

### Formulavimas

* (1) Suprojektuoti pateiktajai užduočiai (problemai) efektyvų lygiagretaus **MPI išskirstyto** vykdymo algoritmą
* (2) ir jį realizuoti MPI priemonėmis

Pirmojoje dalyje aprašomas algoritmo veikimas (pvz., pseudo-kodu, nurodant, kaip skaičiavimai skirstomi į nepriklausomas dalis ir kokiais MPI kreipiniais yra keičiamasi duomenimis.

Jeigu atliekama (2) dalis reikia eksperimentiškai nustatyti algoritmo spartinimo, plečiamumo (scaling) charakteristikas. Pagrįsti gautuosius rezultatus. (1) atveju - naudoti teorinius argumentus.

Atsiskaitant, pateikti (motyvuotą) sprendimo **aprašą**.

Užduoties variantas- toks pat kaip ir 3 užduotyje, arba, pasirenkant kitą - suderinti su dėstytoju. Dėmesio, vietoje "quicksort" ar odd-even sort nagrinėti kitą rikiavimo metodą labiau tinkamą lygiagrečiajam rikiavimui.

Darbinė aplinka programos derinimui ir vykdymui - MIF išskirstytų skaičiavimų klasteris

Skirtingai negu 3 užduotyje reikia:
* sukurti C/C++ programą (testinės programėlės pavyzdys connectivity.c)
* prisijungus prie klasterio programą nusikopijuoti ir sukompiliuoti (pvz., komanda **mpicc -o connectivity connectivity.c**);
* įvykdyti, nurodant procesorių skaičių ne didesnį negu rezervuotų, (pvz., **mpirun -np 4 connectivity**);

Vykdyti eksperimentus rezervuojant bent 32 procesorius. Yra limitai atminčiai ir failų dydžiui ir CPU (sužinoti galima su komanda - limit).
