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

Žemiau pateikta programa leidžia patikrinti java programos vykdymo spartinimą paskirties sistemoje: [TTest.java](./pvz/TTest.java), TTest.class (atnaujinta 2013-12-08).


### Variantai
Variantas **[studijų knygelės nr. paskutinis skaitmuo] arba gretimas (jeigu įdomesnis)**  
Taip pat leidžiama spręsti savo pasirinktą (sunkesnį(*) / įdomesnį) variantą/uždavinį - suderinti su dėstytoju. Galima premija iki 0.5 balo.

Pastaba: nors užduoties algoritmas formuluojamas nuoseklaus algoritmo terminais, tačiau lygiagretusis algoritmas gali skirtis: svarbu išsaugoti esminę nusakyto algoritmo idėją.

<ins>1. Masyvo elementų rūšiavimas "quick sort" metodu.</ins>

<ins>2. Masyvo elementų rūšiavimas "burbuliuko" pavidalo metodu.</ins>  
T.y., metodo esmė - dviejų gretimų elementų sukeitimas vietomis.  
Gugliafrazė: "odd even sort"

<ins>[3. Masyvo elementų rūšiavimas "suliejimo" pavidalo metodu.](./v3/ParallelMergeSortTopDown.java)</ins>  
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
