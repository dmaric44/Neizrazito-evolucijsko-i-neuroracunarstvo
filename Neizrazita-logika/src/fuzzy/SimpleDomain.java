package fuzzy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleDomain extends Domain {
	
	private int first;
	private int last;
	
	SimpleDomain(int first, int last){
		this.first = first;
		this.last = last;
	}

	@Override
	public int getCardinality() {
		return last-first;
	}

	@Override
	public IDomain getComponent(int component) {
		return this;
	}

	@Override
	public int getNumberOfComponents() {
		return 1;
	}

	@Override
	public int indexOfElement(DomainElement element) {
		for(int i=0; i<getLast()-getFirst(); i++) {
			DomainElement el = new DomainElement(new int[] {i+getFirst()});
			if(el.equals(element))
				return i;
		}
		return -1;
	}

	@Override
	public DomainElement elementForIndex(int index) {
		return new DomainElement(new int[] {getFirst()+index});
	}

	public int getFirst() {
		return first;
	}
	public int getLast() {
		return last;
	}

	@Override
	public Iterator<DomainElement> iterator() {
		List<DomainElement> elements = new ArrayList<>();
		for(int i=getFirst(); i<getLast(); i++)
			elements.add(new DomainElement(new int[] {i}));
		return elements.iterator();
	}
	
	
}
