package system;

import fuzzy.*;

public class COADefuzzifier implements Defuzzifier {

	@Override
	public int defuzzify(IFuzzySet set) {
		IDomain domain = set.getDomain();
		
		float counter=0;
		float denominator=0;
		
		for(DomainElement el: domain) {
			double mi = set.getValueAt(el);
			int x = el.getComponentValue(0);
			
			counter += mi*x;
			denominator += mi;
		}
		return Math.round(counter/denominator);
	}
}
