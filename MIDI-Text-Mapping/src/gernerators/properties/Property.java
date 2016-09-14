package gernerators.properties;

/**
 * This class provides a generic definition of what a supported property 
 * in this program should be. It also provides some basic methods for all extenders.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public abstract class Property {
	
	/**
	 * This defines the different types of properties currently supported.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	public enum PropertyType {
		MACRO_ORG,
		MICRO_ORG,
		DURATION,
		VELOCITY,
		SPACING
	}
	
	/** The value tied to this Property */
	protected int value;
	
	/**
	 * There should always be a constructor that accepts a starting 
	 * value for the new Property object.
	 * @param value
	 */
	public Property(int value){
		this.value = value;
	}
	
	/**
	 * This gets the current value of this property.
	 * @return The value of this property.
	 */
	public int getValue(){
		return value;
	}
	
	/**
	 * This method will randomly generate a new value for this Property,
	 * according to the set of values it can be.
	 */
	public abstract void randomize();
	
	/**
	 * This will set the Property's current value as close as it can to the given value.
	 * @param newValue The new value to try and set for this Property.
	 */
	public abstract void setValueToClosest(int newValue);

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
