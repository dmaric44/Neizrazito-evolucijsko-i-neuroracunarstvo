package neuralNetwork;

import dataset.IDataSet;

public class NeuralNetwork {
	private int[] architecture;
	private double[] result;
	
	public NeuralNetwork(int[] architecture) {
		this.architecture = architecture;
		this.result = new double[getNumberOfNeurons()];
		
	}
	
	
	public double[] calcOutput(double[] parameters, double[] input) {
		int currentParameter = 0;
		int currentResult = 0;
		
		//postavljanje ulaza
		result[0] = input[0];
		result[1] = input[1];
		
		//prolaz kroz neurone tipa 1
		for( currentResult=2; currentResult<architecture[1]+2; currentResult++) {
			double suma=0;
			for(int i=0; i<2; i++) {
				suma += Math.abs((result[i]-parameters[currentParameter++]) / parameters[currentParameter++]);
			}
			result[currentResult] = 1 / (1+suma);
		}
		
		for(int layer=2; layer<architecture.length; layer++) {
			int previousLayerNeurons = architecture[layer-1];
			int lastResult = currentResult;
			for(int neuron=0; neuron<architecture[layer]; neuron++) {
				double suma = 0;
				suma += 1*parameters[currentParameter++];
				for(int i=0; i<previousLayerNeurons; i++) {
					suma += result[lastResult - previousLayerNeurons + i]*parameters[currentParameter++];
				}
				result[currentResult++] = sigmoid(suma);
			}
		}
		return new double[] {result[currentResult-3], result[currentResult-2], result[currentResult-1]};
	}
	
	
	


	public double calcError(double[] parameters, IDataSet dataSet) {
		double error = 0;
		
		for(int i=0; i<dataSet.getSizeOfData(); i++) {
			double[] sample = dataSet.getSampleAtIndex(i);
			double[] input = new double[] {sample[0],sample[1]};
			double[] target = new double[] {sample[2], sample[3], sample[4]};
			
			double[] output = calcOutput(parameters, input);
			for(int o=0; o<output.length; o++) {
				error += Math.pow(target[o] - output[o], 2);
			}
		}
		return error / dataSet.getSizeOfData();
	}
	
	
	public void testSolution(double[] coefs, IDataSet dataSet) {
		int score = 0;
		
		for(int i=0; i<dataSet.getSizeOfData(); i++) {
			double[] sample = dataSet.getSampleAtIndex(i);
			double[] input = new double[] {sample[0],sample[1]};
			double[] target = new double[] {sample[2], sample[3], sample[4]};
			
			double[] output = calcOutput(coefs, input);
			for(int o=0; o<output.length; o++) {
				if(output[o]<0.5)
					output[o]=0;
				else
					output[o]=1;
			}
			
			boolean flag = true;
			for(int o=0; o<output.length; o++) {
				if(Double.compare(output[o], target[o]) != 0)
					flag = false;
			}
			
			if(flag)
				score++;
			System.out.println(i+1 +") "+ "Target:" + arrayToString(target) +", Output:" + arrayToString(output) + " -> " + flag);
			
		}
		System.out.println("Accuracy: " + 1.0*score/dataSet.getSizeOfData());
	}
	
	
	
	
	public int getNumberOfNeurons() {
		int numOfNeurons = 0;
		for(int n=0; n<architecture.length; n++)
			numOfNeurons += architecture[n];
		return numOfNeurons;
	}
	
	public int getNumOfParameters() {
		int numOfParameters = 0;
		int type1Neurons = architecture[1];
		numOfParameters += type1Neurons*2*2;
		
		for(int i=2; i<architecture.length; i++) {
			int previousLayer = architecture[i-1];
			int currentLayer = architecture[i];
			numOfParameters += currentLayer * (previousLayer + 1);
		}
		return numOfParameters;
	}
	
	public int[] getArchitecture() {
		return architecture;
	}
	
	private double sigmoid(double net) {
		return 1 / (1 + Math.exp(-net));
	}

	
	private static String arrayToString(double[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(double element: array) {
			sb.append(element+",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		
		return sb.toString();
	}

	
}
