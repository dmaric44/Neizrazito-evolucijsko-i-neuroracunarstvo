package system;

import java.util.List;

import fuzzy.DomainElement;
import fuzzy.IFuzzySet;
import fuzzy.Operations;

public class ProductResolve implements IResolveMethod {

	@Override
	public IFuzzySet resolve(Rule rule, int... values) {

		List<IFuzzySet> ant = rule.getAnt();
		int[] variables = rule.getVariables();
		
		double sc = 1.0;
		for(int i=0; i<ant.size(); i++) {
			double mi = ant.get(i).getValueAt(DomainElement.of(values[variables[i]]));
			sc = sc*mi;
		}
		
		return Operations.unaryOperation(rule.getKons(), Operations.scale(sc));
	}
}
