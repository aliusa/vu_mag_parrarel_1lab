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
