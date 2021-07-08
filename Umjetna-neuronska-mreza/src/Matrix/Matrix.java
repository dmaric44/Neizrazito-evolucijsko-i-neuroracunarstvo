package Matrix;

public class Matrix {

	private double[][] data;
	private int rows;
	private int columns;
	
	public Matrix(int rows, int columns, double leftBound, double rightBound) {
		this.data = new double[rows][columns];
		this.rows = rows;
		this.columns = columns;
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				data[i][j] = leftBound + Math.random()*(rightBound-leftBound);
			}
		}
	}
	
	public Matrix(int rows, int columns) {
		this.data = new double[rows][columns];
		this.rows = rows;
		this.columns = columns;
	}
	
	public void add(double scaler) {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				data[i][j] += scaler;
			}
		}
	}
	
	public void addMatrix(Matrix M) {
		if(columns != M.columns || rows != M.rows) {
			System.out.println("Matrice nisu odgovarajucih dimenzija!");
			return;
		}
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				data[i][j] += M.getData(i,j);
			}
		}
	}
	
	
	public static Matrix transpose(Matrix M) {
		Matrix N = new Matrix(M.columns, M.rows);
		for(int i=0; i<M.rows; i++) {
			for(int j=0; j<M.columns; j++) {
				N.setData(j,i, M.getData(i, j));
			}
		}
		return N;
	}
	
	
	public static Matrix subtract(Matrix A, Matrix B) {
		Matrix N = new Matrix(A.rows, A.columns);
		for(int i=0; i<A.rows; i++) {
			for(int j=0; j<A.columns; j++) {
				N.setData(i, j, A.getData(i, j)-B.getData(i, j));
			}
		}
		return N;
	}
	
	public void multiply(double factor) {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				data[i][j] = data[i][j]*factor;
			}
		}
	}
	
	public void multiply(Matrix M) {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				data[i][j] *= M.getData(i, j);
			}
		}
	}
	
	public static Matrix multiply(Matrix A, Matrix B) {
		if(A.columns != B.rows) {
			System.out.println("Matrice nisu ulanèane");
			return null;
		}
		Matrix N = new Matrix(A.rows, B.columns);
		for(int i=0; i<N.rows; i++) {
			for(int j=0; j<N.columns; j++) {
				double sum = 0;
				for(int k=0; k<A.columns; k++) {
					sum += A.getData(i, k)*B.getData(k, j);
				}
				N.setData(i, j, sum);
			}
		}
		return N;
	}
	
	public void sigmoid() {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				data[i][j] = 1/(1+Math.exp(-data[i][j]));
			}
		}
	}
	
	
	public Matrix dSigmoid() {
		Matrix N = new Matrix(rows, columns);
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				N.data[i][j] = data[i][j]*(1-data[i][j]);
			}
		}
		return N;
	}
	
	
	public static Matrix copy(Matrix A) {
		Matrix N = new Matrix(A.rows, A.columns);
		for(int i=0; i<N.rows; i++) {
			for(int j=0; j<N.columns; j++) {
				N.setData(i, j, A.getData(i, j));
			}
		}
		return N;
	}
	
	public static Matrix fromArray(double[] d) {
		Matrix N = new Matrix(d.length, 1);
		for(int i=0; i<N.rows; i++) {
			N.setData(i, 0, d[i]);
		}
		return N;
	}
	
	public void print() {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				System.out.printf("%f ", data[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public String toFilePrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(rows + " " + columns + "\n");
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				sb.append(data[i][j] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	public double getData(int i, int j) {
		return data[i][j];
	}
	
	public void setData(int i, int j, double value) {
		data[i][j] = value;
	}
}
