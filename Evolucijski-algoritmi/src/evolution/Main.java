package evolution;

import java.io.*;
import java.util.*;

public class Main {	
	
	public static void main(String[] args) throws IOException{
	
		List<double[]> examples = loadExamples(args[0]);
		Random rand = new Random();
		
		int VEL_POP_generationGA = 30;
		int VEL_POP_eliminationGA = 100;					
		int M = VEL_POP_eliminationGA/4;							
		double probabiltyOfMutation = 0.01;
		int maxIterations = 1000;
		boolean elitism = true;

		IGeneticAlgorithm GA = new GenerationGeneticAlgorithm(examples, VEL_POP_generationGA, rand, probabiltyOfMutation, elitism);
//		IGeneticAlgorithm GA = new EliminationGeneticAlgorithm(examples, VEL_POP_eliminationGA, rand, probabiltyOfMutation, M);

	
		Solution sol = GA.findSolution(maxIterations);
		System.out.println("Rješenje:\n" + sol);
		System.out.println("Greska: " + sol.getFitness());
	}
	
	
	
	
	
	
	
	
	private static List<double[]> loadExamples(String path) throws IOException {
		List<double[]> examples = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String st;
		while((st = reader.readLine()) != null){
			String[] splited = st.split("\\s+");
			double[] values = new double[3];
			for(int i=0; i<splited.length; i++) {
				values[i] = Double.parseDouble(splited[i]);
			}
			examples.add(values);
		}
		return examples;
	}


}
