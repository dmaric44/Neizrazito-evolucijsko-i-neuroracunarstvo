package fuzzy;


public abstract class Domain implements IDomain {

	public Domain() {
	}
	
	
	public static IDomain intRange(int first, int last) {
		return new SimpleDomain(first, last);
	}
	
	public static IDomain combine(IDomain d1, IDomain d2) {
		return new CompositeDomain(new IDomain[] {d1,d2});
	}
	
}
