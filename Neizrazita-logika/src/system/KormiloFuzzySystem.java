package system;

import fuzzy.*;

public class KormiloFuzzySystem extends FuzzySystem {
	
	public KormiloFuzzySystem(Defuzzifier def, IResolveMethod method) {
		super(def, method);
		
		IDomain domainK = Domain.intRange(-90, 90+1);
		IDomain udaljenost = Domain.intRange(0, 300+1);
		
		//udaljenost
		IFuzzySet kriticnoBlizu = new CalculatedFuzzySet(udaljenost, StandardFuzzySets.lFunction(
														udaljenost.indexOfElement(DomainElement.of(15)), 
														udaljenost.indexOfElement(DomainElement.of(28))));
		IFuzzySet blizu = new CalculatedFuzzySet(udaljenost, StandardFuzzySets.lambdaFunction(
				udaljenost.indexOfElement(DomainElement.of(20)), 
				udaljenost.indexOfElement(DomainElement.of(45)), 
				udaljenost.indexOfElement(DomainElement.of(65))));
		
		IFuzzySet dalje = new CalculatedFuzzySet(udaljenost, StandardFuzzySets.gammaFunction(
				udaljenost.indexOfElement(DomainElement.of(60)), 
				udaljenost.indexOfElement(DomainElement.of(250))));
		
		
		//kormilo
		IFuzzySet ostroDesno = new CalculatedFuzzySet(domainK, StandardFuzzySets.lFunction(
																domainK.indexOfElement(DomainElement.of(-90)),
																domainK.indexOfElement(DomainElement.of(-85))));
		
		IFuzzySet ostroLijevo = new CalculatedFuzzySet(domainK, StandardFuzzySets.gammaFunction(
				domainK.indexOfElement(DomainElement.of(85)),
				domainK.indexOfElement(DomainElement.of(90))));
		
		//smjer
		IFuzzySet losSmjer = new MutableFuzzySet(Domain.intRange(0, 1+1))
				.set(DomainElement.of(0), 1);
		
		
		//pravila
		rules.add(new Rule(new int[]{0}, kriticnoBlizu, ostroDesno));
		rules.add(new Rule(new int[]{1}, kriticnoBlizu, ostroLijevo));
		
		rules.add(new Rule(new int[]{2}, dalje, ostroLijevo));
		rules.add(new Rule(new int[]{3}, dalje, ostroDesno));
		
		rules.add(new Rule(new int[]{5}, losSmjer, ostroLijevo));
		
	}
	
	public IFuzzySet testRule(int rBr, int ...values) {
		return method.resolve(rules.get(rBr), values);
	}
	
	public IFuzzySet testRulesUnion(int ...values) {
		IFuzzySet set;
		IFuzzySet newSet;
		
		set = method.resolve(rules.get(0), values);
		
		for(int i=1; i<rules.size();i++) {
			newSet = method.resolve(rules.get(i), values);
			set = Operations.binaryOperation(set, newSet, Operations.zadehOr()); 
		}
		
		return set;
	}
	
}
	
