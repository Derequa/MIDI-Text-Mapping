package gernerators;

import gernerators.properties.Organization;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;
import gernerators.properties.Organization.OrgMode;

public class Constant implements Generator {
	
	private Property value;
	
	public Constant(Property constantValue){
		value = constantValue;
	}

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
