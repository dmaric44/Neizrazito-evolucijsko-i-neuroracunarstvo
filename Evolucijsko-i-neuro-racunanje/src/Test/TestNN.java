package Test;

import dataset.DataSet;
import dataset.IDataSet;
import neuralNetwork.NeuralNetwork;

public class TestNN {

	public static void main(String[] args) {
		IDataSet dataSet = new DataSet(args[0]);
		NeuralNetwork NN = new NeuralNetwork(new int[] {2,1,3});
//		System.out.println(NN.getNumberOfNeurons());
		System.out.println(NN.getNumOfParameters());
		
		int numOfParameters = NN.getNumOfParameters();
		double[] params = new double[numOfParameters];
		for(int i=0; i<numOfParameters; i++) {
			params[i] = -1 + Math.random()*2;
		}
		
		for(double p: params)
			System.out.println(p);
		System.out.println();
		for(double p: dataSet.getSampleAtIndex(0))
			System.out.print(p+" ");
		System.out.println();
		
		
		double[] output = NN.calcOutput(params, dataSet.getSampleAtIndex(0));
		for(double p: output)
			System.out.println(p);
	}
	
}
