1. Prisijungiam į MIF SSH
2. `ssh -x hpc`
3. `touch programa.c && chmod -R 0777 programa.c`, edit file
4. `touch job.sh && chmod -R 0777 job.sh`
```shell
#!/bin/bash
#SBATCH -p main
#SBATCH -n64
module load openmpi
mpicc -o programa4 programa4.c
mpirun programa4 64 16777216 fast
```
`-p` short - which queue to send to (`main`, `gpu`, `power`).  
`-n4` - how many processors to reserve (NOTE: if you set the number of cores to be used to x, but actually use fewer cores programmatically, the accounting will still count all the x “requested” cores, so we recommend to calculate this in advance).
5. `sbatch job.sh`
6. `squeue -j JOBPID`
7. `tail -f slurm-JOBPID.out`

`jobid=$(sbatch --parsable job.sh); squeue -j "$jobid"`


Kaip matome iš grafiko, realus spartėjimas nuo 1 iki 16 gijų beveik toks pat. Bet nuo 32 gijų iki 512 jau atsilieka 1-3 kartais.

Tiek teorinis, tiek realus spartėjimas suplokštėja ir nebespartėja
![chart1.png](results/chart1.png)

|                               |           | spartinimas |
|-------------------------------|-----------|-------------|
| n                             | 33554432  |             |
| tserial (sequantial time (T1) | 838860800 |             |
| tparalel 2                    | 436207616 | 1.923076923 |
| tparalel 4                    | 243269632 | 3.448275862 |
| spartinimas 8iems             | 150994944 | 5.555555556 |
| spartinimas 16iems            | 106954752 | 7.843137255 |
| spartinimas 32iems            | 85983232  | 9.756097561 |
| spartinimas 64iems            | 76021760  | 11.03448276 |
| spartinimas 128iems           | 71303168  | 11.76470588 |
| spartinimas 256iems           | 69074944  | 12.14421252 |
| spartinimas 512iems           | 68026368  | 12.33140655 |
