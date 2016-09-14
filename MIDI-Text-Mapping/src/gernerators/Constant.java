package gernerators;

import gernerators.properties.Organization;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;
import gernerators.properties.Organization.OrgMode;

/**
 * This class lets you "generate" the same property over and over.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Constant implements Generator {
	
	// The property to always return
	private Property value;
	
	/**
	 * This sets up the generator with the constant value.
	 * @param constantValue The value to alwasy return.
	 */
	public Constant(Property constantValue){
		value = constantValue;
	}

	/**
	 * This sets up the generator with a randomized property of the given type.
	 * @param mode The type of property to randomize an always return.
	 */
	public Constant(PropertyType mode) {
		switch(mode){
			case MACRO_ORG:		value = new Organization(OrgMode.MACRO);
								break;
			case MICRO_ORG:		value = new Organization(OrgMode.MICRO);
								break;
			case DURATION:		value = new Time();
								break;
			case VELOCITY:		value = new Velocity();
								break;
			case SPACING:		value = new Time();
								break;
			
			default:			break;
		}
		value.randomize();
	}

	/* (non-Javadoc)
	 * @see generators.Generator#getResult()
	 */
	@Override
	public Property getResult() {
		return value;
	}

	/* (non-Javadoc)
	 * @see generators.Generator#step()
	 */
	@Override
	public void step() {}

	/* (non-Javadoc)
	 * @see generators.Generator#getNext()
	 */
	@Override
	public Property getNext() {
		return value;
	}

}
