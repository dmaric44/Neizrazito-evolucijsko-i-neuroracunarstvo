package ANFIS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SamplingFunction {

	public static void main(String[] args) {
		List<double[]> samples = new ArrayList<>();
		for(int x=-4; x<=4; x++) {
			for(int y=-4; y<=4; y++) {
				double target = calculateValue(x, y);
				samples.add(new double[] {x, y, target});
			}
		}
		
		try {
			FileWriter fw = new FileWriter(new File(args[0]));
			for(double[] s: samples) {
				fw.write(s[0] +", " + s[1] +", " + s[2] +"\n");
			}
			fw.flush();
			fw.close();
			System.out.println("Dataset created!");
		}catch(IOException e) {
			System.out.println("Save failed: " + e);
		}
		
	}
	
	private static double calculateValue(double x, double y) {
		return (Math.pow(x-1, 2) + Math.pow(y+2, 2) - 5*x*y + 3) * Math.pow(Math.cos(x/5), 2);
	}
}
