package gernerators.properties;

public abstract class Property {
	
	public static final int ID_TIME = 0;
	public static final int ID_VELOCITY = 1;
	
	protected int value;
	
	public Property(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	public abstract void randomize();
	public abstract void setValueToClosest(int newValue);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Property)) {
			return false;
		}
		Property other = (Property) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}
}
