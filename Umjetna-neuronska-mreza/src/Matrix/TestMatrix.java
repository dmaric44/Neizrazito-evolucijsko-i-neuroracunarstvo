package Matrix;

public class TestMatrix {

	public static void main(String[] args) {
		Matrix A = new Matrix(5,1, -1.2,1.2);
		A.print();
		
		Matrix B = new Matrix(5,1,-1.2,1.2);
		B.print();
		
		Matrix.subtract(A, B).print();
	}
}
