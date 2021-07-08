package ANFIS;

import java.util.ArrayList;
import java.util.List;

public class TestErrorAnfis {

	public static void main(String[] args) {
		List<double[]> samples = ANFIS.loadDataSet(args[0]);
		List<List<Double>> errors = new ArrayList<List<Double>>();
		List<String> labels = new ArrayList<>();
		
		
		//Testiranje za razlièite stope uèenja
		double[] eta = new double[] {0.000001, 0.0001, 100};
		
		for(double e: eta) {
			ANFIS anfis = new ANFIS(10, e, 0.01);
//			List<Double> error = anfis.fitWithError(1000, samples, 1);
			List<Double> error = anfis.fitWithError(10000, samples, samples.size());
			errors.add(error);
			labels.add(String.valueOf(e));
		}
		ANFIS.writeErrors(args[1], errors, labels);
		
		
		//Usporedba pogreške stohastièkog i grupnog naèina izvoðenja
//		ANFIS anfis = new ANFIS(10, 0.0001, 0.01);
//		List<Double> error = anfis.fitWithError(500, samples, 1);
//		errors.add(error);
//		labels.add("Stohastic algorithm");
//
//		ANFIS anfis = new ANFIS(10, 0.001, 0.01);
//		error = anfis.fitWithError(500, samples, samples.size());
//		errors.add(error);
//		labels.add("Batch algorithm");
//		
//		ANFIS.writeErrors(args[1], errors, labels);
		
		
	}
}
