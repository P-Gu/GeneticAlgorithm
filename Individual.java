// Amanda Shen 
// inidividual class for genetic algorithm 

public class Individual {
    private int id;
    private int[] chromosome;
	private double fitness = -1;
    
    // constructor for input type : int[]
    public Individual(int[] newchromosome) {
		this.chromosome = newchromosome;
    }
    // constructor for input type : length
    public Individual(int chromosomeLength) {
        int[] temp = new int[chromosomeLength];
        this.chromosome = RandomSetGene(temp);
    }

    public int[] RandomSetGene (int[] chromosome) {
        for (int i = 0; i < chromosome.length; i++) {
            double random = Math.random();
            if (random < 0.5) {
                chromosome[i] = 0;
            } else {
                chromosome[i] = 1;
            }
        }
        return chromosome;
    }

    // getter and setter
    public int[] getChromosome() {
		return this.chromosome;
    }

    public int getID() {
        return this.id;
    }
    
    public int getChromosomeLength() {
		return this.chromosome.length;
    }
    public int getGene(int index) {
		return this.chromosome[index];
    }
    
    public double getFitness() {
		return this.fitness;
    }
    public void setID (int id) {
        this.id = id;
    }
    
    public void setGene(int index, int value) {
		this.chromosome[index] = value;
    }

    public void setFitness(double fitness) {
		this.fitness = fitness;
    }

    public String toString() {
		String output = "";
		for (int i = 0; i < this.chromosome.length; i++) {
			output += this.chromosome[i];
		}
		return output;
	}
    
 
}