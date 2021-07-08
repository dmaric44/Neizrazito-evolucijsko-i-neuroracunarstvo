package dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSet implements IDataSet {
	private List<double[]> samples; 
	
	public DataSet(String path) {
		samples = loadSamplesFromFile(path);		
	}
	

	@Override
	public int getSizeOfData() {
		return samples.size();
	}

	@Override
	public double[] getSampleAtIndex(int index) {
		return samples.get(index);
	}

	
	
	private List<double[]> loadSamplesFromFile(String path) {
		List<double[]> samples = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();
			while(line != null) {
				String[] splitted = line.split("\\s");
				double[] sample = new double[splitted.length];
				for(int i=0; i<splitted.length; i++) {
					sample[i] = Double.parseDouble(splitted[i]);
				}
				samples.add(sample);
				line = reader.readLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return samples;
	}
	
}
