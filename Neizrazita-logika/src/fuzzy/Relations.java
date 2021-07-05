package fuzzy;

public class Relations {
	
	public static boolean isUtimesURelation(IFuzzySet relation) {
		IDomain domain = relation.getDomain();
		
		if(domain.getNumberOfComponents() == 2) {
			IDomain d1 = domain.getComponent(0);
			IDomain d2 = domain.getComponent(1);
			
			if(d1.getCardinality() == d2.getCardinality()) {
				for(int i=0; i<d1.getCardinality(); i++) {
					if(!d1.elementForIndex(i).equals(d2.elementForIndex(i)))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSymmetric(IFuzzySet relation) {
		if(isUtimesURelation(relation)) {
			IDomain domain = relation.getDomain();
			for(DomainElement el: domain) {
				int[] revValues = new int[el.getNumberOfComponents()];
				int j = el.getNumberOfComponents();
				for(int i=0; i<el.getNumberOfComponents(); i++) {
					revValues[j-1] = el.getComponentValue(i);
					j--;
				}
				DomainElement el2 = DomainElement.of(revValues);
				if(relation.getValueAt(el) != relation.getValueAt(el2)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean isReflexive(IFuzzySet relation) {
		if(isUtimesURelation(relation)) {
			IDomain domain = relation.getDomain();
			
			for(DomainElement el: domain) {
				if(el.getComponentValue(0) == el.getComponentValue(1)) {
					if( Double.compare(relation.getValueAt(el), 1.0) != 0)
						return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean isMaxMinTransitive(IFuzzySet relation) {
		if(isUtimesURelation(relation)) {
			IFuzzySet newRel = compositionOfBinaryRelations(relation, relation);
			
			for(DomainElement el: newRel.getDomain())
				if(relation.getValueAt(el) < newRel.getValueAt(el))
					return false;
			
			return true;
		}
		return false;
	}
	

	public static IFuzzySet compositionOfBinaryRelations(IFuzzySet relation1, IFuzzySet relation2) {
		IDomain U = relation1.getDomain().getComponent(0);
		IDomain W = relation2.getDomain().getComponent(1);
		MutableFuzzySet newRelation = new MutableFuzzySet(Domain.combine(U, W));
		
		for(DomainElement el1: U){
			for(DomainElement el2: W) {
				int i = el1.getComponentValue(0);
				int j = el2.getComponentValue(0);
				double result = 0;
				for(DomainElement el3: relation1.getDomain().getComponent(1)) {
					int k = el3.getComponentValue(0);
					double element1 = relation1.getValueAt(DomainElement.of(i,k));
					double element2 = relation2.getValueAt(DomainElement.of(k,j));
					if(Math.min(element1, element2) > result)
						result = Math.min(element1, element2);
				}
				newRelation.set(DomainElement.of(i,j), result);
			}
		}
		return newRelation;
	}

	public static boolean isFuzzyEquivalence(IFuzzySet relation) {
		return isSymmetric(relation) && isReflexive(relation) && isMaxMinTransitive(relation);
	}
}
