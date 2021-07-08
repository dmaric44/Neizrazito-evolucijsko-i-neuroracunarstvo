package geneticAglorithm;

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
	
	public void setCoefficient(int index, double value) {
		coefs[index] = value;
	}
	
	public double getCoefficient(int index) {
		return coefs[index];
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public double getFitness() {
		return fitness;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for(double value: getCoefs()) {
			sb.append(value+", ");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(')');
		return sb.toString();
	}
	
}
