package fuzzy;

public class DomainElement {
	
	private int[] values;

	public DomainElement(int[] values) {
		this.values = values;
	}
	
	public int getNumberOfComponents() {
		return values.length;
	}
	
	public int getComponentValue(int index) {
		return values[index];
	}
	
	public int hashCode() {
		int result = 0;
		for(int i: values) {
			result += i;
		}
		return result;
	}
	
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof DomainElement) {
			DomainElement element = (DomainElement)obj;
			for(int i=0; i<values.length; i++) {
				if(values[i] != element.getComponentValue(i))
					return false;
			}
			return true;
		}
		return false;
	}

	public String toString() {
		if(values.length>1) {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for(int i: values) {
				sb.append(i+",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
			return sb.toString();
		}
		else {
			return String.valueOf(values[0]);
		}
	}
	
	public static DomainElement of(int ...values) {
		return new DomainElement(values);
	}

}
