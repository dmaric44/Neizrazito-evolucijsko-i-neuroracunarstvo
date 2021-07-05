package fuzzy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CompositeDomain extends Domain {
	
	List<IDomain> components = new ArrayList<>();
	
	CompositeDomain(IDomain[] domains){
		this.components = Arrays.asList(domains);
	}

	@Override
	public int getCardinality() {
		int card = 1;
		for(IDomain d: components) {
			card = card*d.getCardinality();
		}
		return card;
	}

	@Override
	public IDomain getComponent(int component) {
		return components.get(component);
	}

	@Override
	public int getNumberOfComponents() {
		return components.size();
	}

	@Override
	public int indexOfElement(DomainElement element) {
		List<DomainElement> elements = getElements();
		for(int i=0; i<elements.size();i++) {
			if(elements.get(i).equals(element))
				return i;
		}
		return -1;
	}

	@Override
	public DomainElement elementForIndex(int index) {
		List<DomainElement> elements = getElements();
		return elements.get(index);
	}

	@Override
	public Iterator<DomainElement> iterator() {
		List<DomainElement> elements = getElements();
		return elements.iterator();
	}
	
	public List<DomainElement> getElements(){
		List<List<Integer>> elements = new ArrayList<>();
		for(int i=0; i<getNumberOfComponents(); i++) {
			SimpleDomain d = (SimpleDomain) getComponent(i);
			List<Integer> elems = new ArrayList<>();
			for(int j=0; j<d.getCardinality(); j++) {
				elems.add(j+d.getFirst());
			}
			elements.add(elems);
		}

		List<List<Integer>> product = cartesianProduct(elements);

		List<DomainElement> domainElements = new ArrayList<>();
		for(List<Integer> elems: product) {
			int[] values = new int[getNumberOfComponents()];
			for(int i=0; i<elems.size(); i++) {
				values[i] = elems.get(i);
			}
			domainElements.add(new DomainElement(values));
		}
		
		return domainElements;
	}	
		
	
	private static List<List<Integer>> cartesianProduct(List<List<Integer>> elements) {
		List<Integer> d1 = elements.get(0);
		elements.remove(0);
		
		List<List<Integer>> newElements;
		if(elements.size()>1) {
			newElements = cartesianProduct(elements);
		}
		else {
			List<List<Integer>> tuples = new ArrayList<>();
			List<Integer> d2 = elements.get(0);
			for(int i: d1) {
				for(int j: d2) {
					tuples.add(new ArrayList<>(Arrays.asList(i,j)));
				}
			}
			return tuples;
		}
		
		List<List<Integer>> tuples = new ArrayList<>();
		for(int i: d1) {
			for(List<Integer> j: newElements) {
				List<Integer> newEl = new ArrayList<>();
				newEl.addAll(j);
				newEl.add(0, i);
				tuples.add(newEl);
			}
		}
		return tuples;
	}
	

}
