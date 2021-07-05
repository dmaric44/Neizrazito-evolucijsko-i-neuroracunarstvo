package system;

import java.util.*;
import fuzzy.*;

public class Rule {	
	private List<IFuzzySet> ant = new ArrayList<>();
	private IFuzzySet kons;
	private int[] variables;
	
	public Rule(int[] variables, IFuzzySet ...sets) {
		for(int i=0; i<sets.length-1; i++) {
			ant.add(sets[i]);
		}
		kons = sets[sets.length-1];
		this.variables = variables;
	}
	
	public List<IFuzzySet> getAnt(){
		return ant;
	}
	
	public IFuzzySet getKons() {
		return kons;
	}
	
	public int[] getVariables() {
		return variables;
	}
	
//	public IFuzzySet conclude(int ...values) {
//		double min = 1.0;
//		for(int i=0; i<ant.size(); i++) {
//			double mi = ant.get(i).getValueAt(DomainElement.of(values[variables[i]]));
//			min = min*mi;
////			if(mi < min)
////				min = mi;
//		}
//		
//		return Operations.unaryOperation(kons, Operations.scale(min));
//	}
	
}
