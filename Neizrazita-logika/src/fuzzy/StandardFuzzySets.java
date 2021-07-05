package fuzzy;

public class StandardFuzzySets {
	
	StandardFuzzySets(){
	}

	public static IIntUnaryFunction lFunction(int lowerBound, int upperBound) {
		return new IIntUnaryFunction() {
			@Override
			public double valueAt(int value) {
				if(value < lowerBound)
					return 1;
				else if(value >= lowerBound && value < upperBound)
					return (upperBound-value)*1.0 / (upperBound-lowerBound);
				else
					return 0;
			}
		};
	}
	
	public static IIntUnaryFunction gammaFunction(int lowerBound, int upperBound) {
		return new IIntUnaryFunction() {
			@Override
			public double valueAt(int value) {
				if(value < lowerBound)
					return 0;
				else if(value >= lowerBound && value < upperBound)
					return (value-lowerBound)*1.0 / (upperBound-lowerBound);
				else
					return 1;
			}
		};
	}
	
	
	public static IIntUnaryFunction lambdaFunction(int lowerBound, int peak, int upperBound) {
		return  new IIntUnaryFunction() {
			@Override
			public double valueAt(int value) {
				if(value < lowerBound)
					return 0;
				else if(value >= lowerBound && value < peak) 
					return (value-lowerBound)*1.0 / (peak-lowerBound);
				else if(value >= peak && value < upperBound)
					return (upperBound-value)*1.0 /(upperBound-peak);
				else
					return 0;
			}
			
		};
	}
}
