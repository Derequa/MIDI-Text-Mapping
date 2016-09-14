package gernerators.properties;

import java.util.Random;

/**
 * This class implements an organization property with two modes: MACRO and MICRO.
 * This can be used to generate note shifts with varying jumps.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Organization extends Property implements Comparable<Organization> {
	
	/**
	 * This defines the two modes of organization supported.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	public enum OrgMode{
		MICRO,
		MACRO
	}
	
	// A constant for the max not shift allowed in micro mode
	private static final int MAX_SHIFT = 7;
	
	/** An array representing the different kinds of note shifts */
	public static final int[] SHIFTS = {0, 4, 7, -4, -7, 3 , -3};
	/** The index in the shift array for no change */
	public static final int IDX_NO_CHANGE = 0;
	/** The index in the shift array for a small step up */
	public static final int IDX_UP_CONT0 = 1;
	/** The index in the shift array for a large step up */
	public static final int IDX_UP_CONT1 = 2;
	/** The index in the shift array for a small step down */
	public static final int IDX_DWN_CONT0 = 3;
	/** The index in the shift array for a large step down */
	public static final int IDX_DWN_CONT1 = 4;
	/** The index in the shift array for an alternate small step up */
	public static final int IDX_UP_CHNG0 = 5;
	/** The index in the shift array for an alternate small step down */
	public static final int IDX_DWN_CHNG0 = 6;
	
	// The mode this instance is set to
	private OrgMode mode;
	// The RNG for this instance
	private Random r = new Random();

	/**
	 * This constructs an Organization object with the given mode,
	 * and defaults the value to 0.
	 * @param mode The mode to create this instance with.
	 */
	public Organization(OrgMode mode) {
		super(0);
		setMode(mode);
	}
	
	/**
	 * This will get the current mode this Organization object is in.
	 * @return The mode this object is in.
	 */
	public OrgMode getMode(){
		return mode;
	}
	
	/**
	 * This will set this Organization object's mode to the given mode.
	 * @param newMode
	 */
	public void setMode(OrgMode newMode){
		if((newMode == OrgMode.MICRO) || (newMode == OrgMode.MACRO))
			mode = newMode;
		else
			throw new IllegalArgumentException("INVALID ORGANIZATION MODE");
	}
	
	// -----------------------------------------------------------------
	// Overriden Methods
	// -----------------------------------------------------------------
	

	/* (non-Javadoc)
	 * @see generators.properties.Property#randomize()
	 */
	@Override
	public void randomize() {
		switch(mode){
			case MACRO:		setValueToClosest(r.nextInt(17) - 8);
							break;
			case MICRO:		setValueToClosest(r.nextInt(MAX_SHIFT));
							break;
			default:		break;
		}
		
	}

	/* (non-Javadoc)
	 * @see generators.properties.Property#setValueToClosest(int)
	 */
	@Override
	public void setValueToClosest(int newValue) {
		switch(mode){
			case MACRO:		value = newValue;
							break;
			case MICRO:		int minDist = Integer.MAX_VALUE;
							int defaultIndex = IDX_NO_CHANGE;
							for(int i = 0 ; i < SHIFTS.length ; i++){
								if(Math.abs(newValue - SHIFTS[i]) < minDist){
									defaultIndex = i;
									minDist = Math.abs(newValue - SHIFTS[i]);
								}
							}
							value = SHIFTS[defaultIndex];
							break;
			default:		break;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(generators.properties.Organization)
	 */
	@Override
	public int compareTo(Organization arg0) {
		return (new Integer(value)).compareTo(new Integer(arg0.getValue()));
	}

}
