package fuzzy;

public class CalculatedFuzzySet implements IFuzzySet {

	private IDomain domain;
	private IIntUnaryFunction function;
	
	public CalculatedFuzzySet(IDomain domain, IIntUnaryFunction function){
		this.domain = domain;
		this.function = function;
	}
	
	@Override
	public IDomain getDomain() {
		return domain;
	}

	@Override
	public double getValueAt(DomainElement element) {
		int index = domain.indexOfElement(element);
		return function.valueAt(index);
	}

}
