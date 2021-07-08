package Test;

import java.util.Random;

import geneticAglorithm.Evolution;
import geneticAglorithm.Solution;

public class TestGA {

	public static void main(String[] args) {
		double[] coefs = new double[5];
		for(int i=0; i<coefs.length; i++) {
			coefs[i] = 0.5;
		}
		Solution solution = new Solution(coefs);
		System.out.println(solution);
		
		Evolution.mutate2(solution, 0.5, 0.25, new Random());
		
		System.out.println(solution);
		
	}
	
	
	public static void printDArray(double[] array) {
		for(int i=0; i<array.length; i++) {
			System.out.print(array[i]+" ");
		}
		System.out.println();
	}
}
