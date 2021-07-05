package system;

import java.util.ArrayList;
import java.util.List;

import fuzzy.IFuzzySet;
import fuzzy.Operations;

public abstract class FuzzySystem implements IFuzzySystem {
	protected List<Rule> rules;
	private Defuzzifier defuzzifier;
	protected IResolveMethod method;
	
	public FuzzySystem(Defuzzifier defuzzifier, IResolveMethod method) {
		rules = new ArrayList<>();
		this.defuzzifier = defuzzifier;
		this.method = method;
	}

	@Override
	public int resolve(int L, int D, int LK, int DK, int V, int S) {
		IFuzzySet set;
		IFuzzySet newSet;
		

		set = method.resolve(rules.get(0), L,D,LK,DK,V,S);
		
		for(int i=1; i<rules.size();i++) {
			newSet = method.resolve(rules.get(i), L,D,LK,DK,V,S);
			set = Operations.binaryOperation(set, newSet, Operations.zadehOr()); 
		}
		
		return defuzzifier.defuzzify(set);
	}
	
	
	
}
