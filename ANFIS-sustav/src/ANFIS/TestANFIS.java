package ANFIS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestANFIS {

	public static void main(String[] args) {
		int numberOfRules = Integer.parseInt(args[0]);
		List<double[]> samples = ANFIS.loadDataSet(args[1]);
		
		ANFIS anfis = new ANFIS(numberOfRules, 0.0001, 0.01);
		
		anfis.stohasticFit(10000, samples);
//		anfis.batchFit(10000, samples);
		
		List<double[]> predicted =  anfis.predictSamples(samples);
		
		if(args.length > 2)
			anfis.writeFuzzyParametersToFile(args[2]);
		if(args.length > 3)
			anfis.writePredictedValues(args[3], predicted);
	}

	
	
	
}
