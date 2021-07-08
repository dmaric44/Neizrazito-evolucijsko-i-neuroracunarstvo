package evolution;

public class Solution {
	private double coefs[];
	private double fitness;
	
	public Solution(double[] coefs) {
		this.coefs = coefs;
	}

	public double[] getCoefs() {
		return coefs;
	}
	public void setCoefs(double[] coefs) {
		this.coefs = coefs;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public double getFitness() {
		return fitness;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + coefs[0]+" "+coefs[1]+" "+coefs[2]+" "+coefs[3]+" "+coefs[4] +")");
		return sb.toString();
	}
	
}
