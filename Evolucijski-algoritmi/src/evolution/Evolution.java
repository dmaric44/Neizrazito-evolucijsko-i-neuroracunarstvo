package evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Evolution {
	
	public static List<Solution> createPopulation(int VEL_POP, int max, int min, Random rd) {
		List<Solution> population = new ArrayList<>();
		for(int i=0; i<VEL_POP; i++) {
			double[] coefs = new double[5];
			for(int j=0; j<5; j++) {
				coefs[j] = rd.nextDouble()*(max-min)+min;
			}
			population.add(new Solution(coefs));
		}
		return population;
	}
	
	public static double meanSquaredError(Solution solution, List<double[]> examples) {
		double suma = 0;
		for(double[] example : examples) {
			double value = Functions.function(example[0], example[1], solution.getCoefs());
			suma += Math.pow(value - example[2], 2);
		}
		return suma/examples.size();
	}
	
	public static void evaluatePopulation(List<Solution> population, List<double[]> examples) {
		int popSize = population.size();
		for(int i=0; i<popSize; i++) {
			double error = meanSquaredError(population.get(i), examples);
			population.get(i).setFitness(error);
		}
		return;
	}

	public static Solution flatCrossOver(Solution parent1, Solution parent2, Random rd) {
		double[] coefs1 = parent1.getCoefs();
		double[] coefs2 = parent2.getCoefs();
		double[] chCoefs = new double[5];
		
		for(int i=0; i<chCoefs.length; i++) {
			double max = Math.max(coefs1[i], coefs2[i]);
			double min = Math.min(coefs1[i], coefs2[i]);
			chCoefs[i] = rd.nextDouble()*(max-min)+min;
		}
		return new Solution(chCoefs);
	}
	
	public static Solution BLXcrossOver(Solution parent1, Solution parent2, Random rd) {
		double[] coefs1 = parent1.getCoefs();
		double[] coefs2 = parent2.getCoefs();
		double[] chCoefs = new double[5];
		double alpha = 0.5;
		
		for(int i=0; i<chCoefs.length; i++) {
			double max = Math.max(coefs1[i], coefs2[i]);
			double min = Math.min(coefs1[i], coefs2[i]);
			double I = max-min;
			double DG = min-I*alpha;
			double GG = max+I*alpha;
			chCoefs[i] = rd.nextDouble()*(GG-DG)+DG;
		}
		return new Solution(chCoefs);
	}
	
	
	
	public static void mutate(Solution solution, int t, int T, Random rd) { //probati jos neke stvari iz knjige
		double s = rd.nextDouble();
		double b = 5;
		double r = 1 - Math.pow(s, Math.pow(1-(t/T), b));
		int DG=-4, GG=4;
		
		double[] prevCoefs = solution.getCoefs();
		double[] coefs = new double[prevCoefs.length];
		
		for(int i=0; i<prevCoefs.length; i++) {
			double xk = prevCoefs[i];
			double DGM = Math.max(DG, xk-(GG-DG)*r);
			double GGM = Math.min(GG, xk+(GG-DG)*r);
			xk = rd.nextDouble()*(GGM-DGM)+DGM;
			xk = rd.nextDouble()*(GG-DG)+DG;
			coefs[i] = xk;
			
		}
		solution.setCoefs(coefs);
	}
	
	
	public static Solution findBest(List<Solution> population) {
		Solution best = population.get(0);
		for(Solution sol: population) {
			if(sol.getFitness() < best.getFitness())
				best = sol;
		}
		return best;
	}
	
	public static Solution findWorst(List<Solution> population) {
		Solution worst = population.get(0);
		for(Solution sol: population) {
			if(sol.getFitness() > worst.getFitness())
				worst = sol;
		}
		return worst;
	}
	
	public static Solution[] proportionalChoose(List<Solution> population, int numOfPar, Random rd) { 
		Solution[] parents = new Solution[numOfPar];
		double C = findWorst(population).getFitness()+0.1;
		
		double suma=0;
		for(Solution sol: population)
			suma += C-sol.getFitness();
		
		for(int i=0; i<numOfPar; i++) {
			double p = rd.nextDouble();
			double limit = p*suma;
			
			int choosen = 0;
			double upperLimit = C-population.get(choosen).getFitness();
			while(limit > upperLimit && choosen<population.size()-1) {
				choosen++;
				upperLimit += C-population.get(choosen).getFitness();
			}
			parents[i] = population.get(choosen);
		}
		return parents;
	}
	
	
	public static Solution[] getTournamentSolutions(List<Solution> population, int popSize, Random rd) {
		Solution[] tournament = new Solution[3];
		
		Solution p1 = population.get(rd.nextInt(popSize));
		Solution p2 = population.get(rd.nextInt(popSize));
		Solution p3 = population.get(rd.nextInt(popSize));
		
		if(p1.getFitness() < p2.getFitness() && p1.getFitness() < p3.getFitness()) {
			tournament[0] = p1;
			if(p2.getFitness() < p3.getFitness()) {
				tournament[1] = p2;
				tournament[2] = p3;
			}
			else {
				tournament[1] = p3;
				tournament[2] = p2;
			}
		}
		else if(p2.getFitness() < p1.getFitness() && p2.getFitness() < p3.getFitness()) {
			tournament[0] = p2;
			if(p1.getFitness() < p3.getFitness()) {
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
			if(p1.getFitness() < p2.getFitness()) {
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
