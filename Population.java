
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Population {
    private Individual population[];
    private double populationFitness = -1;
    
    public Population(int populationSize) {
		this.population = new Individual[populationSize];
    }
    
    public Population(int populationSize, int chromosomeLength) {
		this.population = new Individual[populationSize];
		for (int i = 0; i < populationSize; i++) {
			Individual individual = new Individual(chromosomeLength); // return a random complete individual
			this.population[i] = individual;
		}
    }
    
    public Individual[] getIndividuals() {
		return this.population;
    }

    public Individual getFittest (int index) {
        SortByFit();
        return this.population[index];
    }

    public void SortByFit() {
		Arrays.sort(this.population, new Comparator<Individual>() {
			public int compare(Individual o1, Individual o2) {
				if (o1.getFitness() > o2.getFitness()) {
					return -1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return 1;
				}
				return 0;
			}
        });
    }

    public double getPopulationFitness() {
		return this.populationFitness;
	}

    public void setPopulationFitness(double fitness) {
		this.populationFitness = fitness;
    }
    
    public int size() {
		return this.population.length;
    }
    
    public Individual setIndividual(int index, Individual individual) {
		return population[index] = individual;
    }
    
    public Individual getIndividual(int index) {
		return population[index];
    }
    
    public void shuffle() {
		Random rnd = new Random();
		for (int i = population.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Individual a = population[index];
			population[index] = population[i];
			population[i] = a;
		}
	}

}