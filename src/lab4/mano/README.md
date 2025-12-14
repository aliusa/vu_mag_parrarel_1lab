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
