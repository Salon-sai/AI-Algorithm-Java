package org.salon.binary;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by sai on 16-11-24.
 */
public class Population
{
    public final static int POPSIZE         = 300;          // 群体中的个体数目
    public final static int MAXGENS         = 1000;         // 最大的后代次数

    private GenoType genomes[] = new GenoType[POPSIZE];     // 群体中的所有个体
    private GenoType best;                                  // 当前的最佳个体
    private GenoType worst;                                 // 当前的最差个体
    private int currentGen = 0;                             // 当前迭代次数

    public Population()
    {
        init();
    }

    /**
     * 群体初始化
     */
    private void init()
    {
        for (int i = 0; i < POPSIZE; i++)
        {
            GenoType genome = new GenoType();
            int random;
            for (int j = 0; j < GenoType.NVARS; j++)
            {
                random = ThreadLocalRandom.current().nextInt(0 , 10);
                if(random % 2 == 0)
                    genome.getGenes()[j] = Boolean.FALSE;
                else
                    genome.getGenes()[j] = Boolean.TRUE;
            }
            genome.setValue();
            this.genomes[i] = genome;
        }
    }

    public GenoType[] getGenomes()
    {
        return genomes;
    }

    public void setGenomes(GenoType[] genomes)
    {
        this.genomes = genomes;
    }

    public GenoType getBest()
    {
        return best;
    }

    public void setBest(GenoType best)
    {
        this.best = best;
    }

    public GenoType getWorst()
    {
        return worst;
    }

    public void setWorst(GenoType worst)
    {
        this.worst = worst;
    }

    public int getCurrentGen()
    {
        return currentGen;
    }

    public void setCurrentGen(int currentGen)
    {
        this.currentGen = currentGen;
    }

    public void incCurrentGen(){
        this.currentGen++;
    }
}
