package ANN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import Matrix.Matrix;

public class NeuralNetwork {
	private List<Matrix> hiddenWeights;
	private Matrix outputWeights;
	private double eta;
	
	public NeuralNetwork(int[] size, double eta, double leftBound, double rightBound) {
		hiddenWeights = new ArrayList<>();
		this.eta = eta;
		
		for(int i=1; i<size.length-1; i++) {
			hiddenWeights.add( new Matrix(size[i], size[i-1], leftBound, rightBound));
		}
		outputWeights = new Matrix(size[size.length-1],  size[size.length-2], leftBound, rightBound);
	}
	
	
	public NeuralNetwork(String fileName) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();
			eta = Double.parseDouble(line);
			
			line = reader.readLine();
			int [] shape = Stream.of(line.split("\\s+"))
					  .mapToInt(Integer::parseInt)
					  .toArray();
			
			outputWeights = new Matrix(shape[0], shape[1]);
			for(int i=0; i<shape[0]; i++) {
				line = reader.readLine();
				double [] row = Stream.of(line.split("\\s+"))
						  .mapToDouble(Double::parseDouble)
						  .toArray();
				for(int j=0; j<shape[1]; j++) {
					outputWeights.setData(i, j, row[j]);
				}
			}
			line = reader.readLine();
			
			hiddenWeights = new ArrayList<>();
			while(line != null) {
				shape = Stream.of(line.split("\\s+"))
						  .mapToInt(Integer::parseInt)
						  .toArray();
				Matrix hidden = new Matrix(shape[0], shape[1]);
				for(int i=0; i<shape[0]; i++) {
					line = reader.readLine();
					double [] row = Stream.of(line.split("\\s+"))
							  .mapToDouble(Double::parseDouble)
							  .toArray();
					for(int j=0; j<shape[1]; j++) {
						hidden.setData(i, j, row[j]);
					}
				}
				hiddenWeights.add(hidden);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}


	public void stohasticFit(int epochs, List<double[]> X, List<double[]> y) {
		for(int i=0; i<epochs; i++) {
			for(int example=0; example<X.size(); example++) {
				List<Matrix> weightsDelta = train(X.get(example), y.get(example));
				addDeltaToWeights(weightsDelta);
			}
			if(i%10 == 0) {
				double MSE = calculateMSE(X,y);
				System.out.println("Iteration:" + i + " MSE = " + MSE);
			}
		}
	}
	
	public void batchFit(int epochs, List<double[]> X, List<double[]> y) {
		List<Matrix> weightsDelta = null;
		for(int i=0; i<epochs; i++) {
			for(int example=0; example<X.size(); example++) {
				List<Matrix> wDelta = train(X.get(example), y.get(example));
				if(weightsDelta != null) {
					for(int m=0; m<weightsDelta.size(); m++)
						weightsDelta.get(m).addMatrix(wDelta.get(m));
				}
				else {
					weightsDelta = new ArrayList<>(wDelta);
				}
			}
			addDeltaToWeights(weightsDelta);
			weightsDelta=null;
			if(i%10 == 0) {
				double MSE = calculateMSE(X,y);
				System.out.println("Iteration:" + i + " MSE = " + MSE);
			}
		}
	}
	
	
	public void miniBatchFit(int epochs, List<double[]> X, List<double[]> y) {
		List<Matrix> weightsDelta = null;
		List<ArrayList<double[]>> X_miniBatch = createMiniBatch(X);
		List<ArrayList<double[]>> y_miniBatch = createMiniBatch(y);
		
		for(int i=0; i<epochs; i++) {
			
			for(int miniG=0; miniG<X_miniBatch.size(); miniG++) {
				List<double[]> currentGroup = X_miniBatch.get(miniG);
				List<double[]> currentLabels = y_miniBatch.get(miniG);
				
				for(int sample=0; sample<currentGroup.size(); sample++) {
					List<Matrix> wDelta = train(currentGroup.get(sample), currentLabels.get(sample));
					if(weightsDelta != null) {
						for(int m=0; m<weightsDelta.size(); m++)
							weightsDelta.get(m).addMatrix(wDelta.get(m));
					}
					else
						weightsDelta = new ArrayList<>(wDelta);
				}
				addDeltaToWeights(weightsDelta);
				weightsDelta = null;
			}
			
			if(i%10 == 0) {
				double MSE = calculateMSE(X,y);
				System.out.println("Iteration:" + i + " MSE = " + MSE);
			}
			
		}
		
		
	}
	
	
	private List<ArrayList<double[]>> createMiniBatch(List<double[]> X) {
		List<ArrayList<double[]>> X_mini = new ArrayList<ArrayList<double[]>>();
		
		for(int i=0; i<20; i+=2) {
			ArrayList<double[]> miniBatch = new ArrayList<>();
			for(int j=0; j<100; j+=20) {
				miniBatch.add(X.get(i+j));
				miniBatch.add(X.get(i+1+j));
			}
			X_mini.add(miniBatch);
		}
		return X_mini;
	}


	private double calculateMSE(List<double[]> X, List<double[]> y) {
		double suma=0;
		for(int i=0; i<X.size(); i++) {
			Matrix output = predict(X.get(i));
			double[] target = y.get(i);

			double error = 0;
			for(int c=0; c<target.length; c++)
				error += target[c]-output.getData(c, 0);
			
			suma += Math.pow(error, 2);
		}
		return suma/X.size();
	}


	private void addDeltaToWeights(List<Matrix> weightsDelta) {
		outputWeights.addMatrix(weightsDelta.get(0));
		int size = weightsDelta.size()-1;
		for(int i=0; i<hiddenWeights.size(); i++) {
			hiddenWeights.get(i).addMatrix(weightsDelta.get(size-i));
		}
	}


	public Matrix predict(double[] x) {
		Matrix input = Matrix.fromArray(x);
		Matrix hidden = Matrix.multiply(hiddenWeights.get(0), input);
		hidden.sigmoid();
		
		for(int i=1; i<hiddenWeights.size(); i++) {
			hidden = Matrix.multiply(hiddenWeights.get(i), hidden);
			hidden.sigmoid();
		}
		
		Matrix output = Matrix.multiply(outputWeights, hidden);
		output.sigmoid();
		return output;
	}
	
	
	public List<Matrix> train(double[] x, double[] y) {
		List<Matrix> weightsDelta = new ArrayList<>();
		List<Matrix> hiddenOutputs = new ArrayList<>();
		Matrix input = Matrix.fromArray(x);
		
		hiddenOutputs.add(input);
		Matrix hidden = Matrix.multiply(hiddenWeights.get(0), input);
		hidden.sigmoid();
		hiddenOutputs.add(hidden);
		
		for(int i=1; i<hiddenWeights.size(); i++) {			
			hidden = Matrix.multiply(hiddenWeights.get(i), hidden);
			hidden.sigmoid();
			hiddenOutputs.add(hidden);
		}
		
		Matrix output = Matrix.multiply(outputWeights, hidden);
		output.sigmoid();
		
		Matrix target = Matrix.fromArray(y);

		Matrix error = Matrix.subtract(target, output);
		Matrix gradient = output.dSigmoid();
		gradient.multiply(error);
		
		Matrix gradient_err = Matrix.copy(gradient);
		
		gradient.multiply(eta);
		
		Matrix hiddenT = Matrix.transpose(hiddenOutputs.get(hiddenOutputs.size()-1));
		
		Matrix whoDelta = Matrix.multiply(gradient, hiddenT);
		weightsDelta.add(whoDelta);

		
		//prolaz po skrivenim slojevima
		Matrix wPrevT = Matrix.transpose(outputWeights);
		error = Matrix.multiply(wPrevT, gradient_err);
		
		for(int i=hiddenWeights.size()-1; i>=0; i--) {
			Matrix hiddenOutput = hiddenOutputs.get(i+1);
			Matrix h_gradient = hiddenOutput.dSigmoid();
		
			h_gradient.multiply(error);
			gradient_err = Matrix.copy(h_gradient);
			h_gradient.multiply(eta);
		
			Matrix inHiddenT = Matrix.transpose( hiddenOutputs.get(i) );
			Matrix whiDelta = Matrix.multiply(h_gradient, inHiddenT);
		
			weightsDelta.add(whiDelta);
			
			wPrevT = Matrix.transpose(hiddenWeights.get(i));
			error = Matrix.multiply(wPrevT, gradient_err);
		}
		return weightsDelta;
	}
	
	public void saveToFile(String path) {
		try {
			FileWriter fw = new FileWriter(new File(path));
			fw.write(eta + "\n");
			String output = outputWeights.toFilePrint();
			fw.write(output);
			
			for(Matrix hiddenWeight: hiddenWeights) {
				output = hiddenWeight.toFilePrint();
				fw.write(output);
			}
			fw.flush();
			fw.close();
		}
		catch(IOException e1) {
			System.out.println("Save failed " + e1);
		}
	}
}
