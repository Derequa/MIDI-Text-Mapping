package gernerators;

import gernerators.properties.Organization;
import gernerators.properties.Organization.OrgMode;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;

/**
 * This class models a Triangular Distribution for generating Properties.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class TriangularDist implements Generator {
	
	/**
	 * This defines the different type of distributions supported.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	public enum DistributionScheme {
		NORMAL,
		LEFT_SLANT,
		RIGHT_SLANT
	}
	
	// The distribution mode for this instance
	private DistributionScheme mode;
	// The last result from this generator
	private Property lastResult;
	// The two workers to generate values
	private Property worker0;
	private Property worker1;
	
	/**
	 * This constructs a triangular generator with the default scheme, and the given type.
	 * @param typeFlag The type of property this generator makes.
	 */
	public TriangularDist(PropertyType typeFlag){
		this(DistributionScheme.NORMAL, typeFlag);
	}
	
	/**
	 * This constructs a triangular generator with the given scheme, and the given type.
	 * @param mode The scheme for this triangular distribution.
	 * @param typeFlag The type of property this generator makes.
	 */
	public TriangularDist(DistributionScheme mode, PropertyType typeFlag){
		this.mode = mode;
		switch(typeFlag){
			case DURATION:		lastResult = new Time();
								worker0 = new Time();
								worker1 = new Time();
								break;
			case SPACING:		lastResult = new Time();
								worker0 = new Time();
								worker1 = new Time();
								break;
			case VELOCITY:		lastResult = new Velocity();
								worker0 = new Velocity();
								worker1 = new Velocity();
								break;
			case MICRO_ORG:		lastResult = new Organization(OrgMode.MICRO);
								worker0 = new Organization(OrgMode.MICRO);
								worker1 = new Organization(OrgMode.MICRO);
								break;
			case MACRO_ORG:		lastResult = new Organization(OrgMode.MACRO);
								worker0 = new Organization(OrgMode.MACRO);
								worker1 = new Organization(OrgMode.MACRO);
								break;
			default:			throw new IllegalArgumentException("TYPE ID NOT RECOGNIZED");
		}
		step();
	}
	
	/**
	 * A way to get the current distribution scheme.
	 * @return The current scheme this is in.
	 */
	public DistributionScheme getMode(){
		return mode;
	}
	
	/**
	 * This is a way to change the mode for this instance.
	 * @param newMode The new mode for this triangular instance.
	 */
	public void changeMode(DistributionScheme newMode){
		if((newMode != DistributionScheme.NORMAL) && 
		   (newMode != DistributionScheme.LEFT_SLANT) && 
		   (newMode != DistributionScheme.RIGHT_SLANT))
			return;
		else
			mode = newMode;
	}

	/* (non-Javadoc)
	 * @see generators.Generator#getResult()
	 */
	@Override
	public Property getResult() {
		return lastResult;
	}

	/* (non-Javadoc)
	 * @see generators.Generator#step()
	 */
	@Override
	public void step() {
		worker0.randomize();
		worker1.randomize();
		switch (mode){
			case NORMAL:		lastResult.setValueToClosest((worker0.getValue() + worker1.getValue()) / 2);
								break;
			case LEFT_SLANT:	if(worker0.getValue() < worker1.getValue())
									lastResult.setValueToClosest(worker0.getValue());
								else
									lastResult.setValueToClosest(worker1.getValue());
								break;
			case RIGHT_SLANT:	if(worker0.getValue() > worker1.getValue())
									lastResult.setValueToClosest(worker0.getValue());
								else
									lastResult.setValueToClosest(worker1.getValue());
								break;
		}
	}

	/* (non-Javadoc)
	 * @see generators.Generator#getNext()
	 */
	@Override
	public Property getNext() {
		step();
		return getResult();
	}
	
}
