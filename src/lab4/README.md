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
