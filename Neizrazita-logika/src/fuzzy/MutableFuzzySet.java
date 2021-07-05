package fuzzy;

public class MutableFuzzySet implements IFuzzySet{
	
	private double[] membership;
	private IDomain domain;
	
	public MutableFuzzySet(IDomain domain){
		this.domain = domain;
		this.membership = new double[domain.getCardinality()];
	}

	@Override
	public IDomain getDomain() {
		return domain;
	}

	@Override
	public double getValueAt(DomainElement element) {
		int index = domain.indexOfElement(element);
		return membership[index];
	}
	
	public MutableFuzzySet set(DomainElement element, double mValue) {
		int index = domain.indexOfElement(element);
		membership[index] = mValue;
		MutableFuzzySet set = new MutableFuzzySet(domain);
		set.membership = membership;
		return set;
	}
}
