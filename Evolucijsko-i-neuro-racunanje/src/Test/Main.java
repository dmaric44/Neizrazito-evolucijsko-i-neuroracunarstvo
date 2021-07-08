package Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dataset.DataSet;
import dataset.IDataSet;
import geneticAglorithm.EliminationGA;
import geneticAglorithm.Solution;
import neuralNetwork.NeuralNetwork;

public class Main {

	public static void main(String[] args) {
		
		IDataSet dataSet = new DataSet(args[0]);
		NeuralNetwork NN = new NeuralNetwork(new int[] {2,8,3});
		System.out.println(NN.getNumberOfNeurons() +" " + NN.getNumOfParameters());
		EliminationGA GA = new EliminationGA(50, NN.getNumOfParameters());
		
		Solution solution = GA.findSolution(NN, dataSet, 20000, 0.01, 4, 0.01, 2, 0.1, 2);
		System.out.println("\n" + solution + "\n");
		
		NN.testSolution(solution.getCoefs(), dataSet);
		
		writeParameters(NN.getArchitecture(), solution.getCoefs(), args[1]);
	}

	
	
	
	
	private static void writeParameters(int[] architecture, double[] coefs, String path) {
		try {
			FileWriter fw = new FileWriter(new File(path));
			StringBuilder sb = new StringBuilder();
			
			for(int neuron: architecture)
				sb.append(neuron + "x");
			sb.deleteCharAt(sb.length()-1);
			fw.write(sb.toString() + "\n");
			
			int numOfType1Neurons = architecture[1]; 
			int currentCoef=0;
			for(int i=0; i<numOfType1Neurons; i++) {
				sb.setLength(0);
				fw.write(coefs[currentCoef++]+" "+coefs[currentCoef++]+" "+coefs[currentCoef++]+" "+coefs[currentCoef++] + "\n");
			}
			
			for(int numOfLayer=2; numOfLayer<architecture.length; numOfLayer++) {
				int neuronsInPreviousLayer = architecture[numOfLayer-1];
				
				
				for(int n=0; n<architecture[numOfLayer]; n++) {
					sb.setLength(0);
					sb.append(coefs[currentCoef++]+" ");
					for(int i=0; i<neuronsInPreviousLayer; i++)
						sb.append(coefs[currentCoef++]+" ");
					
					fw.write(sb.toString()+"\n");
				}
				
			}
			
			
			fw.flush();
			fw.close();
			
		} catch (IOException e) {
			System.out.println("Save failed: " + e);
		}
		
	}
}
