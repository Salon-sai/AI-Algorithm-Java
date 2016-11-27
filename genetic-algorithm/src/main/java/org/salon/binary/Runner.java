package org.salon.binary;

/**
 * Created by sai on 16-11-25.
 */
public class Runner
{

    public static void main(String[] args)
    {
        Environment environment = new Environment();
        Population population = environment.getPopulation();

        environment.evaluate();
        environment.keep_the_best();


        while (population.getCurrentGen() < Population.MAXGENS){
            population.incCurrentGen();
            environment.selection();
            environment.crossover();
            environment.mutate();
            environment.report();
            environment.evaluate();
            environment.elitist();
        }

    }

}
