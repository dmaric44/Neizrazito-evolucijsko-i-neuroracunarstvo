package geneticAglorithm;

import java.util.Random;

import dataset.IDataSet;
import neuralNetwork.NeuralNetwork;

public class EliminationGA {
	private Solution[] population;
	private int popSize;
	private int numOfComponents;
	private Random rd;
	
	
	public EliminationGA(int popSize, int numOfComponents) {
		this.popSize = popSize;
		this.numOfComponents = numOfComponents;
		this.rd = new Random();
	}
	
	
	public Solution findSolution(NeuralNetwork NN, IDataSet dataSet, int numOfGenerations, 
							double pm1, double t1, double pm2, double t2, double pm3, double t3) {
		double[] v = calculateProbabilitiesOfMutations(t1,t2,t3);
		
		population = Evolution.createPopulation(popSize, numOfComponents, rd);
		Evolution.evaluatePopulation(population, NN, dataSet);
		Solution best = Evolution.findBest(population);
		
		int generations = 0;
		int iterations = 0;
		while(best.getFitness() > 10e-7 && numOfGenerations > generations) {

			//krizanje + mutacija
			int[] tournament = Evolution.getTournamentSolutions(population, popSize, rd);
			int worst = tournament[2];
			Solution child = crossParents(population[tournament[0]], population[tournament[1]]);
			
			mutate(child, v, pm1, pm2, pm3);
			child.setFitness(NN.calcError(child.getCoefs(), dataSet));
			population[worst] = child;
			
			
			iterations++;
			if(iterations%popSize == 0) {
				generations++;
				best = Evolution.findBest(population);
				System.out.println("Generacija: " + generations + ", trenutno najbolji mse: " + best.getFitness());
			}
		}
		best = Evolution.findBest(population);
		return best;
	}
	
	
	


	private void mutate(Solution child, double[] v, double pm1, double pm2, double pm3) {
		double p = rd.nextDouble();
		if(p < v[0]) {
			Evolution.mutate1(child, pm1, 0.25, rd);
		}
		else if(p < v[1]) {
			Evolution.mutate1(child, pm1, 0.5, rd);
		}
		else {
			Evolution.mutate2(child, pm3, 1, rd);
		}
	}


	private Solution crossParents(Solution parent1, Solution parent2) {
		double p = rd.nextDouble();
		if(p<0.7) {
			return Evolution.BLXcrossOver(parent1, parent2, rd);
		}
		else if(p<0.85){
			return Evolution.flatCrossOver(parent1, parent2, rd);
		}
		else {
			return Evolution.discreteCrossover(parent1, parent2, rd);
		}
	}

	
	private double[] calculateProbabilitiesOfMutations(double t1, double t2, double t3) {
		double suma = t1 + t2 + t3;
		return new double[] {t1/suma, t2/suma, t3/suma};
	}

	public void printPopulation() {
		for(Solution s: population)
			System.out.println(s);
	}
}
