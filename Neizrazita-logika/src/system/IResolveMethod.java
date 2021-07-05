package system;

import fuzzy.IFuzzySet;

public interface IResolveMethod {

	public IFuzzySet resolve(Rule rule, int ...values);
}
