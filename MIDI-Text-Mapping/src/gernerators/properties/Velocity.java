package gernerators.properties;

import java.util.Random;

/**
 * This class implements velocity property for use with a generator.
 * This can be used to create note velocities.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Velocity extends Property {
	
	/** A constant for the max velocity for a note */
	public static final int MAX_VELOCITY = 127;
	/** A constant for the default velocity for this property */
	public static final int DEFAULT_VELOCITY = 96;
	/** A constant for the minimum velocity for a note */
	public static final int MIN_VELOCITY = 36;
	private Random r = new Random();
	
	/**
	 * This constructs a velocity property with the default velocity.
	 */
	public Velocity(){
		this(DEFAULT_VELOCITY);
	}

	/**
	 * This constructs a velocity property with the given value.
	 * @param value The value to initialize this property with.
	 */
	public Velocity(int value) {
		super(value);
		if((value > MAX_VELOCITY) || (value < MIN_VELOCITY))
			super.value = DEFAULT_VELOCITY;
	}

	/* (non-Javadoc)
	 * @see generators.properties.Property#randomize()
	 */
	@Override
	public void randomize() {
		value = r.nextInt(MAX_VELOCITY - MIN_VELOCITY + 1) + MIN_VELOCITY;
	}

	/* (non-Javadoc)
	 * @see generators.properties.Property#setValueToClosest(int)
	 */
	@Override
	public void setValueToClosest(int newValue) {
		if(newValue < MIN_VELOCITY)
			value = MIN_VELOCITY;
		else if(newValue > MAX_VELOCITY)
			value = MAX_VELOCITY;
		else
			value = newValue;
	}

}
