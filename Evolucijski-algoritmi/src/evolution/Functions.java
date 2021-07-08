package evolution;

public class Functions {

	
	public static double function(double x, double y, double[] coef) {
		return Math.sin(coef[0] + coef[1]*x) + coef[2]*Math.cos(x*(coef[3]+y))*(1/(1+Math.exp(Math.pow(x-coef[4],2))));
	}
}
