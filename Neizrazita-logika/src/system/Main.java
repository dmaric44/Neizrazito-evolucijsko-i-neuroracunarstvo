package system;

import java.io.*;
import java.util.Scanner;

import fuzzy.Operations;


public class Main {
	public static int MAX_D = 300;
	public static int MAX_V = 120;
	
	public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


        Defuzzifier def = new COADefuzzifier();
        IResolveMethod method = new MinimumResolve();
        IFuzzySystem fsAkcel = new AkcelFuzzySystem(def, method);
        IFuzzySystem fsKormilo = new KormiloFuzzySystem(def, method);
        
        
	    int L=0,D=0,LK=0,DK=0,V=0,S=0,akcel,kormilo;
	    String line = null;
		while(true){
			if((line = input.readLine())!=null){
				if(line.charAt(0)=='K') break;
				Scanner s = new Scanner(line);
				L = s.nextInt();
				D = s.nextInt();
				LK = s.nextInt();
				DK = s.nextInt();
				V = s.nextInt();
				S = s.nextInt();
	        }
			
			L = scale(L, MAX_D);
			D = scale(D, MAX_D);
			LK = scale(LK, MAX_D);
			DK = scale(DK, MAX_D);
			V = scale(V, MAX_V);
			
			akcel = fsAkcel.resolve(L,D,LK,DK,V,S);
			kormilo = fsKormilo.resolve(L,D,LK,DK,V,S);
			
	        //akcel = 0; //kormilo = -20;
	        System.out.println(akcel + " " + kormilo);
	        System.out.flush();
	   }
	}

	private static int scale(int x, int max) {
		return Math.min(x, max);
	}
	
}
