package geneticAglorithm;

import java.util.Random;

import dataset.IDataSet;
import neuralNetwork.NeuralNetwork;

public class Evolution {
	
	public static Solution[] createPopulation(int popSize, int numOfComponents, Random rd) {
		Solution[] population = new Solution[popSize];
		for(int s=0; s<popSize; s++) {
			double[] components = new double[numOfComponents];
			for(int i=0; i<numOfComponents; i++) {
				components[i] = -1 + rd.nextDouble()*2;
			}
			population[s] = new Solution(components);
		}
		return population;
	}
	
	
	public static void evaluatePopulation(Solution[] population, NeuralNetwork NN, IDataSet dataSet) {
		for(int i=0; i<population.length; i++) {
			double error = NN.calcError(population[i].getCoefs(), dataSet);
			population[i].setFitness(error);
		}
		return;
	}

	public static Solution flatCrossOver(Solution parent1, Solution parent2, Random rd) {
		double[] component1 = parent1.getCoefs();
		double[] component2 = parent2.getCoefs();
		double[] chComponents = new double[component1.length];
		
		for(int i=0; i<chComponents.length; i++) {
			double max = Math.max(component1[i], component2[i]);
			double min = Math.min(component1[i], component2[i]);
			chComponents[i] = rd.nextDouble()*(max-min)+min;
		}
		return new Solution(chComponents);
	}
	
	public static Solution BLXcrossOver(Solution parent1, Solution parent2, Random rd) {
		double[] component1 = parent1.getCoefs();
		double[] component2 = parent2.getCoefs();
		double[] chComponents = new double[component1.length];
		double alpha = 0.5;
		
		for(int i=0; i<chComponents.length; i++) {
			double max = Math.max(component1[i], component2[i]);
			double min = Math.min(component1[i], component2[i]);
			
			double I = max-min;
			double DG = min-I*alpha;
			double GG = max+I*alpha;
			
			chComponents[i] = DG + rd.nextDouble()*(GG-DG);
		}
		return new Solution(chComponents);
	}
	
	public static Solution discreteCrossover(Solution parent1, Solution parent2, Random rd) {
		double[] component1 = parent1.getCoefs();
		double[] component2 = parent2.getCoefs();
		double[] chComponents = new double[component1.length];
		
		for(int i=0; i<chComponents.length; i++) {
			if(rd.nextDouble() <= 0.5)
				chComponents[i] = component1[i];
			else
				chComponents[i] = component2[i];
		}
		return new Solution(chComponents);
	}
	
	
	public static void mutate1(Solution solution, double pm, double var, Random rd) {
		for(int i=0; i<solution.getCoefs().length; i++) {
			if(rd.nextDouble() <= pm) {
				double value = solution.getCoefficient(i);
				solution.setCoefficient(i, value + rd.nextGaussian()*var);
			}
		}
	}
	
	public static void mutate2(Solution solution, double pm, double var, Random rd) {
		for(int i=0; i<solution.getCoefs().length; i++) {
			if(rd.nextDouble() <= pm) {
				solution.setCoefficient(i, rd.nextGaussian()*var);
			}
		}
	}
	
	
	public static Solution findBest(Solution[] population) {
		Solution best = population[0];
		for(Solution sol: population) {
			if(sol.getFitness() < best.getFitness())
				best = sol;
		}
		return best;
	}
	
	public static Solution findWorst(Solution[] population) {
		Solution worst = population[0];
		for(Solution sol: population) {
			if(sol.getFitness() > worst.getFitness())
				worst = sol;
		}
		return worst;
	}
	
	
	
	public static int[] getTournamentSolutions(Solution[] population, int popSize, Random rd) {
		int[] tournament = new int[3];
		
		int p1 = rd.nextInt(popSize);
		int p2 = rd.nextInt(popSize);
		int p3 = rd.nextInt(popSize);
		
		if(population[p1].getFitness() < population[p2].getFitness() && population[p1].getFitness() < population[p3].getFitness()) {
			tournament[0] = p1;
			if(population[p2].getFitness() < population[p3].getFitness()) {
				tournament[1] = p2;
				tournament[2] = p3;
			}
			else {
				tournament[1] = p3;
				tournament[2] = p2;
			}
		}
		else if(population[p2].getFitness() < population[p1].getFitness() && population[p2].getFitness() < population[p3].getFitness()) {
			tournament[0] = p2;
			if(population[p1].getFitness() < population[p3].getFitness()) {
				tournament[1] = p1;
				tournament[2] = p3;
			}
			else {
				tournament[1] = p3;
				tournament[2] = p1;
			}
		}
		else{
			tournament[0] = p3;
			if(population[p1].getFitness() < population[p2].getFitness()) {
				tournament[1] = p1;
				tournament[2] = p2;
			}
			else {
				tournament[1] = p2;
				tournament[2] = p1;
			}
		}
		return tournament;
	}
}
