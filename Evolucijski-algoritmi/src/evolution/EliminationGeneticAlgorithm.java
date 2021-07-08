package evolution;

import java.util.List;
import java.util.Random;

public class EliminationGeneticAlgorithm implements IGeneticAlgorithm {
	private List<double[]> examples;
	private int popSize;
	private int M;
	private List<Solution> population;
	private Random rd;
	private double probabiltyOfMutation;
	
	public static final int MAX = 4;
	public static final int MIN = -4;
	
	public EliminationGeneticAlgorithm(List<double[]> examples, int popSize, Random rd, double probabiltyOfMutation, int M) {
		this.examples = examples;
		this.popSize = popSize;
		this.M = M;
		this.rd = rd;
		this.probabiltyOfMutation = probabiltyOfMutation;
	}


	public Solution findSolution(int numOfIterations) {
		System.out.println("Elimination Genetic Algorithm");
		population = Evolution.createPopulation(popSize, MAX,MIN, rd);
		Evolution.evaluatePopulation(population, examples);
		Solution best = Evolution.findBest(population);
		
		for(int g=1; g<=numOfIterations; g++) {
			for(int m=0; m < M; m++) {
				Solution[] tournament = Evolution.getTournamentSolutions(population, popSize, rd);
				int worst = population.indexOf(tournament[2]);
				Solution child = Evolution.BLXcrossOver(tournament[0], tournament[1], rd);
				if(rd.nextDouble() < probabiltyOfMutation) {
					Evolution.mutate(child, g, numOfIterations, rd);
				}
				child.setFitness(Evolution.meanSquaredError(child, examples));
				population.set(worst, child);
			}
			
			
			Solution genBest = Evolution.findBest(population);
			if(genBest.getFitness() < best.getFitness()) {
				best = genBest;
				System.out.println("Iter: " + g + " -> " + "greska: " + best.getFitness());
			}
		}
		
		
		return Evolution.findBest(population);
	}


	
}
