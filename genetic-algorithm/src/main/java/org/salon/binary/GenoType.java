package org.salon.binary;

/**
 * Created by sai on 16-11-24.
 */
public class GenoType
{

    public final static int NVARS = 10;                 // 基因的个数
    public final static double PXOVER      = 0.8;      // 杂交概率
    public final static double PMUTATION   = 0.015;    // 变异概率
    private final static double LBOUND      = -20;        // 取值范围下限
    private final static double UBOUND      = 20;       // 取值范围上限

    private Boolean[] genes = new Boolean[NVARS];       // 染色体
    private double value;                               // 根据基因体和取值范围计算出的变量值
    private double fitness;                             // 该染色体的适应度
    private double rfitness;                            // 该染色体的相关适应度
    private double cfitness;                            // 该染色体的总体相关适应度

    public Boolean[] getGenes()
    {
        return genes;
    }

    public void setGenes(Boolean[] genes)
    {
        this.genes = genes;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public double getFitness()
    {
        return fitness;
    }

    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }

    public double getRfitness()
    {
        return rfitness;
    }

    public void setRfitness(double rfitness)
    {
        this.rfitness = rfitness;
    }

    public double getCfitness()
    {
        return cfitness;
    }

    public void setCfitness(double cfitness)
    {
        this.cfitness = cfitness;
    }

    public void setValue()
    {
        double sum = 0;
        for (int i = NVARS - 1, j = 0; i >= 0; i--, j++){
            if (this.genes[i])
                sum += Math.pow(2, j);
        }
        this.value = LBOUND + (sum * (UBOUND - LBOUND) / (Math.pow(2, NVARS) - 1));
    }

    /**
     * 深拷贝
     * @param original
     * @return
     */
    public static GenoType copyOf(GenoType original)
    {
        GenoType copy = new GenoType();
        copy.setValue(original.getValue());
        copy.setRfitness(original.getRfitness());
        copy.setCfitness(original.getCfitness());
        copy.setFitness(original.getFitness());
        for (int i = 0; i < NVARS; i++)
        {
            copy.getGenes()[i] = original.getGenes()[i];
        }
        return copy;
    }
}
