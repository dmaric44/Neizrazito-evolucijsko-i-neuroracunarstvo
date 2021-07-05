package system;

import fuzzy.IFuzzySet;

public interface Defuzzifier {
	public int defuzzify(IFuzzySet set);
}
