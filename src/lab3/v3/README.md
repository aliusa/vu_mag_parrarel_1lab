## 3. Masyvo elementų rūšiavimas "suliejimo" pavidalo metodu
(Galite nustatyti, kad masyvo dydis 2-ju laipsnis).  
Gugliafrazė: "merge sort"  

<p>
Tema: Lygiagretus masyvo rikiavimas "merge sort" metodu.
</p>

<p>
Aprašymas:
Programa demonstruoja, kaip masyvas rikiuojamas keliomis gijomis,
kurios veikia lygiagrečiai. Darbas paskirstomas dinamiškai –
kai gija baigia rikiuoti savo dalį, ji gali būti panaudota kitiems darbams.
</p>
<p>
Kvietimo pvz.:
<code>
java ParallelMergeSort 4 1048576 fast
</code>

Parametrai:  
`args[0]` – gijų skaičius  
`args[1]` – masyvo dydis (turi būti 2^k)  
`args[2]` – režimas: "debug" arba "fast"

## Testavimas
Testavimas atliktas 100 kartų išvedant vidurkius.

| Masyvo dydis      | Greitis (ms) 1 gijos | Greitis (ms) 2 gijos | Greitis (ms) 3 gijos | Greitis (ms) 4 gijos | Greitis (ms) 6 gijos | Greitis (ms) 8 gijos | Greitis (ms) 16 gijų | Greitis (ms) 32 gijos |
|:------------------|---------------------:|---------------------:|---------------------:|---------------------:|---------------------:|---------------------:|---------------------:|----------------------:|
| 2^14 (16384)      |                    1 |                    1 |                    0 |                    0 |                    1 |                    1 |                    2 |                     4 |
| 2^15 (32768)      |                    2 |                    1 |                    1 |                    1 |                    1 |                    1 |                    2 |                     5 |
| 2^16 (65536)      |                    5 |                    3 |                    3 |                    3 |                    2 |                    2 |                    3 |                     3 |
| 2^17 (131072)     |                   12 |                    6 |                    6 |                    6 |                    4 |                    4 |                    4 |                     6 |
| 2^18 (262144)     |                   23 |                   12 |                   12 |                   12 |                    8 |                    8 |                    7 |                     8 |
| 2^19 (524288)     |                   54 |                   30 |                   26 |                   29 |                   16 |                   15 |                   12 |                    12 |
| 2^20 (1048576)    |                  112 |                   58 |                   53 |                   58 |                   33 |                   34 |                   23 |                    21 |
| 2^21 (2097152)    |                  205 |                  111 |                  118 |                  110 |                   68 |                   63 |                   42 |                    35 |
| 2^22 (4194304)    |                  429 |                  218 |                  233 |                  220 |                  138 |                  130 |                   82 |                    60 |
| 2^23 (8388608)    |                  940 |                  501 |                  481 |                  505 |                  271 |                  282 |                  175 |                   129 |
| 2^24 (16777216)   |                 1941 |                 1005 |                  978 |                  999 |                  560 |                  557 |                  353 |                   269 |
| 2^25 (33554432)   |                 3920 |                 2066 |                 2057 |                 2064 |                 1134 |                 1168 |                  720 |                   558 |

Tarp 1 ir 2 gijų skirtumas ženklus.  
Kaip matome tarp 2 ir 4 gijų toks pat greitis.

### Analizė

## Pagreitėjimas

Analizuojant 2^25 dydžio duomenų masyvą, teorinius spartėjimas skaičiuotas pagal formulę `Sreal(p)=n*LOG(n,2)`.  
`t(2)=n/2*LOG(n/2,2)+n`, `spartėjimas==Sreal(p)/t(2)`  
`t(4)=n/4*LOG(n/4,2)+n/2+n`, `spartėjimas=Sreal(p)/t(4)`  
`t(16)=n/8*LOG(n/8,2)+n/4+n/2+n`, `spartėjimas=Sreal(p)/t(8)`  
`t(16)=n/16*LOG(n/16,2)+n/8+n/4+n/2+n`, `spartėjimas=Sreal(p)/t(16)`  
`t(32)=n/32*LOG(n/32,2)+n/16+n/8+n/4+n/2+n`, `spartėjimas=Sreal(p)/t(32)`

| Masyvo dydis         | 1 gija | 2 gijos | 4 gijos | 8 gijos | 16 gijų | 32 gijos |
|:---------------------|-------:|--------:|--------:|--------:|--------:|---------:|
| Realus spartėjimas   |   1.00 |    1.90 |    1.87 |    3.23 |    5.13 |     6.46 |
| Teorinis spartėjimas |   1.00 |    1.92 |    3.45 |    5.56 |    7.84 |     9.76 |

Matome, kad tik nuo 8 branduolių pagreitėja. 

![Chart](./results/chart1.png "Chart")



Atlikus testavimą su kiekviena gija nuo 1 iki 32 - gaunam kurkas išreikšmingesnį laiptuotą grafiką:
![Chart](./results/chart2.png "Chart")
