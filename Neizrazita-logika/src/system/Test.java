package system;

import fuzzy.*;

public class Test {

	public static void main(String[] args) {

		IDomain domain = Domain.intRange(-15, 15);
		IFuzzySet set = new CalculatedFuzzySet(domain, StandardFuzzySets.lambdaFunction(
														domain.indexOfElement(DomainElement.of(-10)),
														domain.indexOfElement(DomainElement.of(0)),
														domain.indexOfElement(DomainElement.of(10))));
		
		set = Operations.unaryOperation(set, Operations.scale(0.1));
		Debug.print(set, "Set:");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		IDomain d = Domain.intRange(6,14+1);
//		IFuzzySet s1 = new CalculatedFuzzySet(d, StandardFuzzySets.lambdaFunction(d.indexOfElement(DomainElement.of(6)),
//																	 d.indexOfElement(DomainElement.of(9)),
//																	 d.indexOfElement(DomainElement.of(12))));
//		IFuzzySet s2 = new CalculatedFuzzySet(d, StandardFuzzySets.lambdaFunction(d.indexOfElement(DomainElement.of(10)),
//				 d.indexOfElement(DomainElement.of(12)),
//				 d.indexOfElement(DomainElement.of(14))));
//		
//		IFuzzySet union = Operations.binaryOperation(s1, s2, Operations.zadehOr());
//		Debug.print(union, "union");
//		IFuzzySet setMin = Operations.unaryOperation(union, Operations.min(0.4));
//		Debug.print(setMin, "setMin");
//		double val = cdef.defuzzify(union);
//		System.out.println(Math.round(val));
	}
}
