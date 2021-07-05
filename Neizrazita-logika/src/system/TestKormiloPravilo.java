package system;

import java.util.Scanner;

import fuzzy.Debug;
import fuzzy.IFuzzySet;

public class TestKormiloPravilo {

	
	public static void main(String[] args) {
		Defuzzifier COADef = new COADefuzzifier();
		KormiloFuzzySystem kormilo = new KormiloFuzzySystem(COADef, new MinimumResolve());
		
		Scanner s = new Scanner(System.in);
		System.out.println("Unesite vrijednosti redni broj pravila i ulaze L,D,LK,DK,V i S:");
		int rbr = s.nextInt();
		int L = s.nextInt();
		int D = s.nextInt();
		int LK = s.nextInt();
		int DK = s.nextInt();
		int V = s.nextInt();
		int S = s.nextInt();
		
		IFuzzySet set = kormilo.testRule(rbr, L,D,LK,DK,V,S);
		Debug.print(set, "Zakljucak:");
		System.out.println(COADef.defuzzify(set));
	}
}
