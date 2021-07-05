package system;

import java.util.List;

import fuzzy.*;

public class MinimumResolve implements IResolveMethod {

	@Override
	public IFuzzySet resolve(Rule rule, int... values) {

		List<IFuzzySet> ant = rule.getAnt();
		int[] variables = rule.getVariables();
		
		double min = 1.0;
		for(int i=0; i<ant.size(); i++) {
			double mi = ant.get(i).getValueAt(DomainElement.of(values[variables[i]]));
			if(mi < min)
				min = mi;
		}
		
		return Operations.unaryOperation(rule.getKons(), Operations.slice(min));
	}

	
}
