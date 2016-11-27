package org.salon.binary;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by sai on 16-11-24.
 */
public class Environment
{

    private Population population;

    public Environment()
    {
        this.population = new Population();
        init();
    }

    private void init()
    {
        for (GenoType geno: this.population.getGenomes())
        {
            double fitness = targetFun(geno.getValue());
            geno.setFitness(fitness);
        }
    }

    /**
     * 尋找最佳個體
     */
    public void keep_the_best()
    {
        GenoType[] genomes = this.population.getGenomes();
        int best_pos       = 0;

        for (int i = 1; i < Population.POPSIZE; i++)
            if (genomes[best_pos].getFitness() < genomes[i].getFitness())
                best_pos = i;

        // 深拷貝一份到最佳元素上
        population.setBest(GenoType.copyOf(genomes[best_pos]));
    }

    /**
     * 计算每个个体的适应度
     */
    public void evaluate()
    {
        for (GenoType genome: this.population.getGenomes())
        {
            genome.setFitness(targetFun(genome.getValue()));
        }
    }

    /**
     * 筛选出适合的个体，并重置到群体当中
     */
    public void selection()
    {
        int mem;
        double sum = 0;
        double p;
        GenoType newGenomes[] = new GenoType[Population.POPSIZE];

        // 计算群体总适应度
        for (mem = 0; mem < Population.POPSIZE; mem++)
            sum += this.population.getGenomes()[mem].getFitness();


        for (mem = 0; mem < Population.POPSIZE; mem++)
        {
            GenoType genome = this.population.getGenomes()[mem];
            genome.setRfitness(genome.getFitness() / sum);
        }

        // 计算个体的累积适应度
        this.population.getGenomes()[0]
                .setCfitness(this.population.getGenomes()[0]
                        .getRfitness());
        for (mem = 1; mem < Population.POPSIZE; mem++)
        {
            GenoType genome         = this.population.getGenomes()[mem];
            GenoType genomeBefore   = this.population.getGenomes()[mem - 1];

            genome.setCfitness(genomeBefore.getCfitness() + genome.getRfitness());
        }

        // 获取随机概率把适应度高的个体筛选出来
        for (int i = 0; i < Population.POPSIZE; i++)
        {
            p = ThreadLocalRandom.current().nextInt(0 , 1000) / 1000.0;
            if (this.population.getGenomes()[0].getRfitness() > p)
            {
                newGenomes[i] = GenoType.copyOf(this.population.getGenomes()[0]);
            }
            GenoType before = null;
            GenoType current= null;
            for (int j = 1; j < Population.POPSIZE; j++)
            {
                before = this.population.getGenomes()[j - 1];
                current= this.population.getGenomes()[j];
                if (before.getCfitness() <= p && current.getCfitness() > p)
                {
                    newGenomes[i] = GenoType.copyOf(current);
                }
            }
        }
        population.setGenomes(newGenomes);
    }

    /**
     * 选择两个个体进行杂交
     */
    public void crossover()
    {
        int mem;
        int one = 0;
        int first = 0;
        double x;

        for (mem = 0; mem < Population.POPSIZE; mem++)
        {
            x = ThreadLocalRandom.current().nextInt(1000) / 1000;
            if (x < GenoType.PXOVER)
            {
                ++first;
                if (first % 2 == 1)
                    one = mem;
                else
                    Xover(one, mem);
            }
        }
    }

    /**
     * 获取两个个体的位置将其进行杂交
     * @param one 个体位置
     * @param two 个体位置
     */
    public void Xover(int one, int two)
    {
        int point;      // 交换点的位置
        Boolean[] g1 = this.population.getGenomes()[one].getGenes();
        Boolean[] g2 = this.population.getGenomes()[two].getGenes();
        if (GenoType.NVARS > 1)
        {
            if (GenoType.NVARS == 2)
            {
                point = 1;
            }
            else
                point = ThreadLocalRandom.current().nextInt(GenoType.NVARS - 1);
                swap(g1, g2, point, GenoType.NVARS);
        }
    }

    /**
     * 根据选择的杂交点进行交换操作
     * @param g1 第一个个体的染色体
     * @param g2 第二个个体的染色体
     * @param start 杂交点
     * @param end   杂交结束点
     */
    public void swap(Boolean[] g1, Boolean[] g2, final int start, final int end)
    {
        for (int i = start; i < end; i++)
        {
            Boolean temp = g1[i];
            g1[i] = g2[i];
            g2[i] = temp;
        }
    }

    /**
     * 进行基因变异
     */
    public void mutate()
    {

        for (int i = 0; i < Population.POPSIZE; i++)
        {
            for (int j = 0; j < GenoType.NVARS; j++)
            {
                double p = ThreadLocalRandom.current().nextInt(1000) / 1000;
                if (p < GenoType.PMUTATION)
                {
                    this.population.getGenomes()[i].getGenes()[j] =
                            !this.population.getGenomes()[i].getGenes()[j];
                }
            }
        }
    }

    public void report()
    {
        double bestVal;
        double avg;
        double stddev;
        double sum_square   = 0.0;      // 全部个体的适应度的平方总和
        double square_sum;              // 总和的平方的平均值
        double sum          = 0.0;
        double cur_fitness;

        for (int i = 0; i < Population.POPSIZE; i++){
            cur_fitness = this.population.getGenomes()[i].getFitness();
            sum += cur_fitness;
            sum_square = cur_fitness * cur_fitness;
        }

        avg = sum / Population.POPSIZE;
        square_sum = avg * avg * Population.POPSIZE;
        stddev = Math.sqrt(Math.abs(sum_square - square_sum) / (Population.POPSIZE - 1));

        bestVal = this.population.getBest().getFitness();

        System.out.println("\n generation : " + this.population.getCurrentGen() +
                " , best value : " + this.population.getBest().getValue() +
                " , best fitness : " + bestVal +
                " , avg of sum : " + avg + ", " + stddev + "\n\n");
    }

    /**
     * 选出最佳并加入到群体当中
     */
    public void elitist()
    {
        double best, worst;
        int best_mem        = 0;
        int worst_mem       = 0;

        best    = this.population.getGenomes()[best_mem].getFitness();
        worst   = this.population.getGenomes()[worst_mem].getFitness();

        GenoType[] genomes = this.population.getGenomes();
        for (int i = 0; i < Population.POPSIZE - 1; i++)
        {
            if (genomes[i].getFitness() > genomes[i + 1].getFitness())
            {
                if (genomes[i].getFitness() >= best){
                    best = genomes[i].getFitness();
                    best_mem = i;
                }
                if (genomes[i + 1].getFitness() <= worst){
                    worst = genomes[i + 1].getFitness();
                    worst_mem = i + 1;
                }
            }else{
                if (genomes[i + 1].getFitness() >= best){
                    best = genomes[i + 1].getFitness();
                    best_mem = i + 1;
                }
                if (genomes[i].getFitness() <= worst){
                    worst = genomes[i].getFitness();
                    worst_mem = i;
                }
            }
        }

        /**
         * 如果新的群体中的最佳个体比上一代的最佳个体适应度，则将当前最佳的个体保存到
         * 第POPSIZE位置上。否则将上一代最佳元素替换当前最差适应度的个体
         */
        if (best >= this.population.getBest().getFitness()){
            this.population.setBest(GenoType.copyOf(genomes[best_mem]));
        }
        else
        {
            genomes[worst_mem] = GenoType.copyOf(this.population.getBest());
        }
    }

    /**
     * 现在目标函数为x^2
     * @param x 自变量
     * @return 目标函数的值
     */
    private double targetFun(double x)
    {
        return -6 *x * x + 1;
    }

    public Population getPopulation()
    {
        return population;
    }

    public void setPopulation(Population population)
    {
        this.population = population;
    }
}