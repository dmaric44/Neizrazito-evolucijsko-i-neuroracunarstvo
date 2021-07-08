package ANN;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.SwingUtilities;

import gestureWindow.GUI;

public class ANNTrainGestures {

	public static void main(String[] args) {

		List<double[]> X = new ArrayList<>();
		List<double[]> y = new ArrayList<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			String line = reader.readLine();
			while(line != null) {
				String[] splited = line.split(",\\s+");
				
				double[] x = new double[2*30];						
				for(int i=0; i<splited.length-1; i++) {
					double d = Double.parseDouble(splited[i]);
					x[i] = d;
				}
				X.add(x);
				
				double[] output = Stream.of(splited[splited.length-1].split(","))
						  .mapToDouble(Double::parseDouble)
						  .toArray();
				y.add(output);
				
				line = reader.readLine();
				
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		NeuralNetwork ANN = new NeuralNetwork(new int[] {2*30,10,6,5}, 0.5, -0.5,0.5);
		
		ANN.stohasticFit(1000, X, y);
//		ANN.batchFit(1000, X, y);
//		ANN.miniBatchFit(1000, X, y);
		
		if(args.length > 1) {
			ANN.saveToFile(args[1]);
		}
		
		SwingUtilities.invokeLater(()->{
			new GUI(ANN).setVisible(true);
		});
	}
}
