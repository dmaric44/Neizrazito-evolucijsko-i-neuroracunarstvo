package ANN;

import java.util.ArrayList;
import java.util.List;

import Matrix.Matrix;

public class TestNeuralNetwork {

	public static void main(String[] args) {
		NeuralNetwork ann = new NeuralNetwork(new int[] {1,6,6,1}, 0.5, -1.2, 1.2);

		double [][] X_1 = { {-1}, {-0.8}, {-0.6}, {-0.4}, {-0.2}, {0}, {0.2}, {0.4}, {0.6}, {0.8}, {1} };
		double [][] y_1 = { {1}, {0.64}, {0.36}, {0.16}, {0.04}, {0}, {0.04}, {0.16}, {0.36}, {0.64}, {1} };
		
		List<double[]> X = new ArrayList<>();
		for(double[] x: X_1)
			X.add(x);
		
		List<double[]> Y = new ArrayList<>();
		for(double[] y: y_1)
			Y.add(y);
	
		double [][] X1 = { {-0.9}, {-0.5}, {0.5}};
		
		
		for(double[] x: Y) {
			System.out.println(x[0]);
		}
	
//		ann.batchFit(120000, X, Y);
		ann.stohasticFit(120000, X, Y);
		
		System.out.println("Predikcije primjera:");
		for(int i=0; i<X.size(); i++) {
			Matrix output = ann.predict(X.get(i));
			System.out.println("("+X.get(i)[0]+","+Y.get(i)[0]+") -> " + output.getData(0, 0));
		}
		
		System.out.println("Test");
		for(double[] x: X1) {
			Matrix output = ann.predict(x);
			System.out.println(x[0] + " -> " + output.getData(0, 0));
		}
		
	}
}
