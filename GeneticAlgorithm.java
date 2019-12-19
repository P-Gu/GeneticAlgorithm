
// Genetic Algorithm implemetation -- Amanda Shen last edit 

public class GeneticAlgorithm {
    private int pop_size;
    private int good_chromosome_count;
    private int t_size;
    private double m_rate;
    private double c_rate;

    public GeneticAlgorithm (int pop_size, int good_chromosome_count, int t_size, double m_rate, double c_rate) {
        this.pop_size = pop_size;
        this.good_chromosome_count = good_chromosome_count;
        this.t_size = t_size;
        this.m_rate = m_rate;
        this.c_rate = c_rate;
    }

    public Population newPopulation (int chromosome_size) {
        Population pop = new Population(this.pop_size,chromosome_size );
        return pop;
    }

    public double Individual_fitnessScore (Individual individual, Maze maze) {
        int[] current = individual.getChromosome();
        Robot robot = new Robot(current, maze, 100); // 100 is maxmoves
        robot.run();
        double result = maze.scoreRoute(robot.getRoute());
        individual.setFitness(result);
        return result;
    }

    public double Population_fitnessScore (Population population, Maze maze) {
        double result = 0;
        for (Individual each : population.getIndividuals()) {
            result += this.Individual_fitnessScore(each, maze);
        }
        population.setPopulationFitness(result);
        return result;
    }

    public boolean ExceedMaxGeneration (int current, int max) {
        boolean result = current > max;
        return result;
    }

    public Individual BestParent (Population population) {
        Population newPop = new Population(this.t_size);
        population.shuffle();
        for (int i = 0; i < this.t_size; i++) {
			Individual new_individual = population.getIndividual(i);
			newPop.setIndividual(i, new_individual);
        } 
        return newPop.getFittest(0);
    }

    public Population mutate (Population population) {
        Population newPop = new Population(this.pop_size);

        for (int i = 0; i < population.size(); i++) {
            // get current chromosome as individual 
            Individual indi = population.getFittest(i);
            // loop throught its gene and mutate accordingly 
            for (int j = 0; j < indi.getChromosomeLength(); j++) {
                if (i >= this.good_chromosome_count) {
                    if (this.m_rate >= Math.random()) {
                        int newGene = 1;
                        if (indi.getGene(j) == 1) {
                            newGene = 0;
                        }
			// mutate gene
                        indi.setGene(j, newGene);
                    }
                    
                }

            }
            // add new possible mutated chromosome back to population 
            newPop.setIndividual(i, indi);
        }

        return newPop;
    }

    public Population crossover (Population population) {
        Population newPop = new Population(this.pop_size);

        for (int i = 0; i < population.size(); i++) {
            // get current chromosome as individual 
            Individual p1 = population.getFittest(i);
            // loop throught its gene and mutate accordingly 
            if (i >= this.good_chromosome_count) {
                if (this.c_rate >= Math.random()) {
                    Individual child = new Individual(p1.getChromosomeLength());
                    Individual p2 = this.BestParent(population);
                    int swapPoint = (int) (Math.random() * (p1.getChromosomeLength() + 1));
                    //cross over: 
				    for (int j = 0; j < p1.getChromosomeLength(); j++) {
                        if (j < swapPoint) {
                            child.setGene(j, p1.getGene(j));
                        } else {
                            child.setGene(j, p2.getGene(j));
                        }
                    }
                    // add new child to new population 
				    newPop.setIndividual(i, child);
                }
            } else {
                newPop.setIndividual(i, p1);
            }
        }
        return newPop;
    }
}
