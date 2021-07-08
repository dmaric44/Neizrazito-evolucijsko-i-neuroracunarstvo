package ANN;

public class TestNeuralNetworkImport {

	public static void main(String[] args) {
		NeuralNetwork ann = new NeuralNetwork("C:\\Users\\Admin\\Desktop\\ann.txt");
		
		double [][] X1 = { {-0.9}, {-0.5}, {0.5}};
		ann.predict(X1[0]).print();
		ann.predict(X1[1]).print();
		ann.predict(X1[2]).print();
	}
}
