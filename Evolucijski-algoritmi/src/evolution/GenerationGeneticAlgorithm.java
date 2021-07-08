package evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerationGeneticAlgorithm implements IGeneticAlgorithm {
	private List<double[]> examples;
	private int popSize;
	private List<Solution> population;
	private boolean elitism;
	private Random rd;
	private double probabiltyOfMutation;
	public static final int MAX = 4;
	public static final int MIN = -4;

	public GenerationGeneticAlgorithm(List<double[]> examples, int VEL_POP, Random rd, double probabiltyOfMutation, boolean elitism) {
		this.examples = examples;
		this.popSize = VEL_POP;
		this.elitism = elitism;
		this.rd = rd;
		this.probabiltyOfMutation = probabiltyOfMutation;
	}
	
	@Override
	public Solution findSolution(int numOfIterations) {
		System.out.println("Generation Genetic Algorithm");
		int VEL_POP = popSize;		
		population = Evolution.createPopulation(VEL_POP, MAX,MIN, rd);
		Evolution.evaluatePopulation(population, examples);
		Solution best = Evolution.findBest(population);
		
		if(elitism)
			VEL_POP--;
		
		for(int g=1; g<=numOfIterations; g++) {
			List<Solution> newPopulation = new ArrayList<>();
			Solution bestGen = Evolution.findBest(population);
			if(elitism)
				newPopulation.add(bestGen);
			
			for(int i=0; i<VEL_POP;i++) {
				Solution[] parents = Evolution.proportionalChoose(population, 2, rd);
				Solution child = Evolution.BLXcrossOver(parents[0], parents[1], rd);
				if(rd.nextDouble() < probabiltyOfMutation) {
					Evolution.mutate(child, g, numOfIterations, rd);
				}
				newPopulation.add(child);
			}
			Evolution.evaluatePopulation(newPopulation, examples);
			bestGen = Evolution.findBest(newPopulation);
			if(bestGen.getFitness() < best.getFitness()) {
				best = bestGen;
				System.out.println("Iter: " + g + " -> " + "greska: " +best.getFitness());
			}
			population = newPopulation;
			
		}
		
		return Evolution.findBest(population);
	}

}
