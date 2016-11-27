package org.salon.binary;

import org.junit.Test;

/**
 * Created by sai on 16-11-24.
 */
public class TestPopulation
{

    @Test
    public void test()
    {
        Population population = new Population();
        GenoType[] genomes = population.getGenomes();
        for (GenoType genome : genomes)
        {
            System.out.println(genome.getValue());
        }
    }
}
