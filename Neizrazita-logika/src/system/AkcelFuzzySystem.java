package system;

//import java.util.ArrayList;
//import java.util.List;

import fuzzy.*;

public class AkcelFuzzySystem extends FuzzySystem {
	
	public AkcelFuzzySystem(Defuzzifier def, IResolveMethod method) {
		super(def, method);
		
		IDomain domainAkc = Domain.intRange(-35,36);
		IDomain brzina = Domain.intRange(0, 121);
		IDomain udaljenost = Domain.intRange(0, 301);
		
		//akceleracija
		IFuzzySet uspori = new CalculatedFuzzySet(domainAkc, StandardFuzzySets.lFunction(
				domainAkc.indexOfElement(DomainElement.of(-35)),
				domainAkc.indexOfElement(DomainElement.of(-25))));

		IFuzzySet odrzavaj = new CalculatedFuzzySet(domainAkc, StandardFuzzySets.lambdaFunction(
				domainAkc.indexOfElement(DomainElement.of(-10)),
				domainAkc.indexOfElement(DomainElement.of(0)),
				domainAkc.indexOfElement(DomainElement.of(10)))); 
		
		IFuzzySet ubrzaj = new CalculatedFuzzySet(domainAkc, StandardFuzzySets.gammaFunction(
				domainAkc.indexOfElement(DomainElement.of(25)),
				domainAkc.indexOfElement(DomainElement.of(35))));
		
		//brzina
		IFuzzySet sporo = new CalculatedFuzzySet(brzina, StandardFuzzySets.lFunction(
															brzina.indexOfElement(DomainElement.of(20)), 
															brzina.indexOfElement(DomainElement.of(55))));
		IFuzzySet normalno =  new CalculatedFuzzySet(brzina, StandardFuzzySets.lambdaFunction(
																brzina.indexOfElement(DomainElement.of(50)),
																brzina.indexOfElement(DomainElement.of(80)),
																brzina.indexOfElement(DomainElement.of(95))));
		IFuzzySet brzo = new CalculatedFuzzySet(brzina, StandardFuzzySets.gammaFunction(
															brzina.indexOfElement(DomainElement.of(90)), 
															brzina.indexOfElement(DomainElement.of(120))));
		
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
		
		
		//pravila
		rules.add(new Rule(new int[]{4}, sporo, ubrzaj));
		
		rules.add(new Rule(new int[]{0}, kriticnoBlizu, ubrzaj));
		rules.add(new Rule(new int[]{1}, kriticnoBlizu, ubrzaj));
		
		rules.add(new Rule(new int[]{1,3,4}, blizu, blizu, brzo, uspori));
		rules.add(new Rule(new int[]{0,2,4}, blizu, blizu, brzo, uspori));
		
//		rules.add(new Rule(new int[]{0,1,4}, dalje, dalje, nijeBrzo, ubrzaj));
	}
	
	
}
