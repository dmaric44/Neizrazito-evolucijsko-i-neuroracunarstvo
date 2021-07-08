package Test;

import dataset.DataSet;
import dataset.IDataSet;

public class TestDataSet {

	public static void main(String[] args) {
		IDataSet dataSet = new DataSet(args[0]);
		
		for(int i=0; i<dataSet.getSizeOfData(); i++) {
			double[] samples = dataSet.getSampleAtIndex(i);
			for(double s: samples)
				System.out.print(s + " ");
			System.out.println();
		}
	}
}
