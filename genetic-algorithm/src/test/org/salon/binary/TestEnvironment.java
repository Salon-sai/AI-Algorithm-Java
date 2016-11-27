package org.salon.binary;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.notification.RunListener;

/**
 * Created by sai on 16-11-25.
 */
public class TestEnvironment
{

    @Test
    public void testEnvironment()
    {
        Environment environment = new Environment();

        environment.evaluate();
        environment.selection();

        Population population   = environment.getPopulation();

        for (GenoType genome : population.getGenomes())
        {
            System.out.println(genome.getValue() + " : " + genome.getFitness());
        }
    }

    @Test
    public void testSwap()
    {
        Environment environment = new Environment();
        Boolean[] g1 = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
        Boolean[] g2 = {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};

        environment.swap(g1, g2, 2, 3);

        Assert.assertArrayEquals(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}, g1);
    }

    @Test
    public void testMutate()
    {
        Boolean actual = Boolean.FALSE;
        Assert.assertEquals(Boolean.TRUE, !actual);
    }

}
