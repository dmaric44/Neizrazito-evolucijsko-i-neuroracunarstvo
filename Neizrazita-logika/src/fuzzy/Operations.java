package fuzzy;

public class Operations {
	

	public static IFuzzySet unaryOperation(IFuzzySet set1, IUnaryFunction function) {
		return new IFuzzySet (){

			@Override
			public IDomain getDomain() {
				return set1.getDomain();
			}

			@Override
			public double getValueAt(DomainElement element) {
				return function.valueAt(set1.getValueAt(element));
			}
		};		
	}
	
	
	
	public static IFuzzySet binaryOperation(IFuzzySet set1, IFuzzySet set2, IBinaryFunction function) {
		return new IFuzzySet() {

			@Override
			public IDomain getDomain() {
				return set1.getDomain();
			}

			@Override
			public double getValueAt(DomainElement element) {
				return function.valueAt(set1.getValueAt(element), set2.getValueAt(element));
			}
		};		
	}
	
	
	
	public static IUnaryFunction zadehNot() {
		return new IUnaryFunction() {
			@Override
			public double valueAt(double value) {
				return 1-value;
			}
		};
	}
	
	public static IBinaryFunction zadehAnd() {
		return new IBinaryFunction() {
			@Override
			public double valueAt(double val1, double val2) {
				return Math.min(val1, val2);
			}
		};
	}
	
	public static IBinaryFunction zadehOr() {
		return new IBinaryFunction() {
			@Override
			public double valueAt(double val1, double val2) {
				return Math.max(val1, val2);
			}
		};
	}
	
	public static IBinaryFunction hamacherTNorm(double parameter) {
		return new IBinaryFunction() {
			@Override
			public double valueAt(double a, double b) {
				return (a*b) / (parameter + (1-parameter)*(a+b-a*b));
			}
		};
	}
	
	public static IBinaryFunction hamacherSNorm(double parameter) {
		return new IBinaryFunction() {
			@Override
			public double valueAt(double a, double b) {
				return (a+b-(2-parameter)*a*b) / (1-(1-parameter)*a*b);
			}
		};
	}
	
	public static IUnaryFunction slice(double min) {
		return new IUnaryFunction() {
			@Override
			public double valueAt(double value) {
				return Math.min(min, value);
			}
		};
	}
	
	public static IUnaryFunction scale(double sc) {
		return new IUnaryFunction() {
			@Override
			public double valueAt(double value) {
				return value*sc;
			}
		};
	}
	
}
