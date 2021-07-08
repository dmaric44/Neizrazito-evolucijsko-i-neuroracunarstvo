package ANFIS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Matrix.Matrix;

public class ANFIS {
	private double eta_a;
	private double eta_b;
	private Matrix A;
	private Matrix B;
	private Matrix layer4;
	Matrix[] deltas = null;

	
	
	public ANFIS(int numOfRules, double eta_a, double eta_b) {
		this.eta_a = eta_a;
		this.eta_b = eta_b;
		A = initLayer1(numOfRules, 2);
		B = initLayer1(numOfRules, 2);
		layer4 = initLayer4(numOfRules, 3);

	}
	
	
	public void fit(int numOfIterations, List<double[]> samples, int size) {
		for(int epoch=0; epoch<numOfIterations; epoch++) {
			int counter = 0;
			for(int i=0; i<samples.size(); i++) {
				Matrix[] delta = train(samples.get(i));
				if(deltas == null) {
					deltas = delta;
				}
				else {
					addDelta(delta);
				}
				counter++;
				if(counter >= size) {
					updateParameters();
					counter = 0;
					deltas=null;
				}
			}
			deltas=null;
			if(epoch%100 == 0) {
				System.out.println("Iter:" + epoch + " MSE:" + meanSquaredError(samples));
			}
		}
	}
	
	public void stohasticFit(int numOfIterations, List<double[]> samples) {
		fit(numOfIterations, samples, 1);		
	}
	
	public void batchFit(int numOfIterations, List<double[]> samples) {
		fit(numOfIterations, samples, samples.size());		
	}
	
	

	public double predict(double[] sample) {
		double x = sample[0];
		double y = sample[1];
//		double target = sample[2];

		Matrix alpha = membershipFunctionValue(x, A);
		Matrix beta = membershipFunctionValue(y, B);
		
		Matrix pi = alpha.multiply(beta);
		
		double normalizeSum = Matrix.matrixSum(pi);
		Matrix piNormalised = pi.multiply(1/normalizeSum);
		
		Matrix input = Matrix.vector(x,y,1);
		Matrix outputs = piNormalised.multiply(Matrix.multiply(layer4, input));
		
		return Matrix.matrixSum(outputs);
	}

	
	
	
	public Matrix[] train(double[] sample) {
		double x = sample[0];
		double y = sample[1];
		double target = sample[2];

		Matrix alpha = membershipFunctionValue(x, A);
		Matrix beta = membershipFunctionValue(y, B);
		Matrix pi = alpha.multiply(beta);
		
		double normalizeSum = Matrix.matrixSum(pi);
		Matrix piNormalised = pi.multiply(1/normalizeSum);
		
		Matrix input = Matrix.vector(x,y,1);
		Matrix z = Matrix.multiply(layer4, input);
		
		Matrix outputs = piNormalised.multiply(Matrix.multiply(layer4, input));
		double output = Matrix.matrixSum(outputs);
		
		
		double error = target-output;
		Matrix deltaLayer4 = Matrix.multiply(pi, Matrix.transpose(input));
		deltaLayer4 = deltaLayer4.multiply(1/normalizeSum);
		deltaLayer4 = deltaLayer4.multiply(eta_b*error);
		
		
		Matrix deltaZ = new Matrix(z.getRows(), z.getColumns());
		for(int i=0; i<deltaZ.getRows(); i++) {
			double z_i = z.getData(i, 0);
			double suma = 0;
			for(int j=0; j<z.getRows(); j++) {
				if(i!=j)
					suma += pi.getData(j, 0)*(z_i - z.getData(j, 0));
			}
			deltaZ.setData(i, 0, suma / Math.pow(normalizeSum, 2));
		}
		
		
		
		Matrix deltaA = new Matrix(deltaZ.getRows(), 2);
		Matrix deltaB = new Matrix(deltaZ.getRows(), 2);
						
		for(int i=0; i<deltaZ.getRows(); i++) {
			deltaA.setData(i, 0, eta_a*error*deltaZ.getData(i, 0)*beta.getData(i, 0)*A.getData(i, 1)*(1-alpha.getData(i, 0))*alpha.getData(i, 0));
			deltaA.setData(i, 1, eta_b*error*deltaZ.getData(i, 0)*beta.getData(i, 0)*(x-A.getData(i, 0))*(1-alpha.getData(i, 0))*alpha.getData(i, 0));
		
			deltaB.setData(i, 0, eta_a*error*deltaZ.getData(i, 0)*alpha.getData(i, 0)*B.getData(i, 1)*(1-beta.getData(i, 0))*beta.getData(i, 0));
			deltaB.setData(i, 1, eta_b*error*deltaZ.getData(i, 0)*alpha.getData(i, 0)*(y-B.getData(i, 0))*(1-beta.getData(i, 0))*beta.getData(i, 0));
		
		}
		
		
//		deltaA.print();
//		deltaB.print();
		
		return new Matrix[] {deltaLayer4, deltaA, deltaB};
		
	}
	
	


	
	


	private Matrix membershipFunctionValue(double x, Matrix M) {
		Matrix N = new Matrix(M.getRows(), 1);
		for(int i=0; i<A.getRows(); i++) {
			N.setData(i, 0, 1 / (1 + Math.exp(M.getData(i, 1)*(x-M.getData(i, 0)))) );
		}
		return N;
	}
	
	private void updateParameters() {
		layer4.addMatrix(deltas[0]);
		A.addMatrix(deltas[1]);
		B.addMatrix(deltas[2]);
	}


	private void addDelta(Matrix[] delta) {
		for(int i=0; i<delta.length; i++) {
			deltas[i].addMatrix(delta[i]);
		}
	}


	private double meanSquaredError(List<double[]> samples) {
		double sum = 0;
		for(double[] sample: samples)
			sum += Math.pow(predict(sample)-sample[2], 2);
		
		return sum/samples.size();
	}
	
	
	private Matrix initLayer4(int rows, int columns) {
		Matrix functionParameters = new Matrix(rows, columns);
		for(int i=0; i<rows; i++) {
			functionParameters.setData(i, 0, Math.random()-0.5);
			functionParameters.setData(i, 1, Math.random()-0.5);
			functionParameters.setData(i, 2, 0);
//			for(int j=0; j<columns; j++) {
//				functionParameters.setData(i, j, Math.random()-0.5);
//			}
		}
		return functionParameters;
	}


	private Matrix initLayer1(int rows, int columns) {
		Matrix fuzzySet = new Matrix(rows, columns);
		for(int i=0; i<rows; i++) {
			fuzzySet.setData(i, 0, Math.random()-0.5);
			fuzzySet.setData(i, 1, Math.random()-0.5);
		}
		return fuzzySet;
	}


	public void writeFuzzyParametersToFile(String path) {
		try {
			FileWriter fw = new FileWriter(new File(path));
			for(int i=0; i<A.getRows(); i++) {
				fw.write(A.getData(i, 0) + ", " + A.getData(i, 1)+ ", "+ B.getData(i, 0)+", "+ B.getData(i, 1) + ", " +
						layer4.getData(i, 0) +", " + layer4.getData(i, 1) +", " + layer4.getData(i, 2) +"\n");
			}
			fw.flush();
			fw.close();
			System.out.println("Parametri uspješno zaspisani!");
		} catch (IOException e) {
			System.out.println("Save failed: " + e);
		}
	}
	
	public void writePredictedValues(String path, List<double[]> predicted) {
		try {
			FileWriter fw = new FileWriter(new File(path));
			for(double[] sample: predicted) {
				fw.write(sample[0] +", " + sample[1] +", " + sample[2] + "\n");
			}
			fw.flush();
			fw.close();
			System.out.println("Parametri uspješno zaspisani!");
		} catch (IOException e) {
			System.out.println("Save failed: " + e);
		}
	}

	public static void writeErrors(String path, List<List<Double>> errors, List<String> labels) {
		try {
			FileWriter fw = new FileWriter(new File(path));
			int label=0;
			for(List<Double> error: errors) {
				for(Double err: error) {
					fw.write(String.valueOf(err)+", ");
				}
				fw.write(labels.get(label) + "\n");
				label++;
			}
			fw.flush();
			fw.close();
			System.out.println("Parametri uspješno zaspisani!");
		} catch (IOException e) {
			System.out.println("Save failed: " + e);
		}
	}

	public List<double[]> predictSamples(List<double[]> samples) {
		List<double[]> predicted = new ArrayList<>();
		for(double[] s: samples) {
			double output = predict(s);
			predicted.add(new double[] {s[0],s[1],output});
		}
		return predicted;
	}


	public static List<double[]> loadDataSet(String path) {
		List<double[]> samples = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();
			while(line != null) {
				String[] splitted = line.split(",\\s+");
				double[] sample = new double[3];
				for(int i=0; i<3; i++) {
					sample[i] = Double.parseDouble(splitted[i]);
				}
				samples.add(sample);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return samples;
	}
	
	


	public List<Double> fitWithError(int numOfIterations, List<double[]> samples, int size) {
		List<Double> errors = new ArrayList<>();
		
		for(int epoch=0; epoch<numOfIterations; epoch++) {
			int counter = 0;
			for(int i=0; i<samples.size(); i++) {
				Matrix[] delta = train(samples.get(i));
				if(deltas == null) {
					deltas = delta;
				}
				else {
					addDelta(delta);
				}
				counter++;
				if(counter >= size) {
					updateParameters();
					counter = 0;
					deltas=null;
				}
			}
			deltas=null;
			double MSE = meanSquaredError(samples);
			errors.add(MSE);
			if(epoch%100 == 0) {
				System.out.println("Iter:" + epoch + " MSE:" + MSE);
			}
		}
		return errors;
	}
	
	public void setEtaA(double eta) {
		this.eta_a = eta;
	}


	
}
