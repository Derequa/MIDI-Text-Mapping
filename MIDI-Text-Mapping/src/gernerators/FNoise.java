package gernerators;

import gernerators.properties.Organization;
import gernerators.properties.Organization.OrgMode;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;

/**
 * This class implements a generator that approximates the 1/f Noise compositional algorithm.
 * NOTE: This is not an exact model as 1/F Noise requires you add the results of the dice.
 * However, due to how values are set for properties in this model, setting the value to the sum would produce
 * undesirable behavior that favored the high values for each property. Therefore the property's value is set to
 * the average of the dice. Which should result in behavior that APPROXIMATES 1/f Noise.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class FNoise implements Generator {
	
	/** A constant for the default number of dice */
	public static final int DEFAULT_DICE = 4;
	/** A constant for the max number of dice */
	public static final int MAX_DICE = 32;
	/** A constant for the minimum number of dice */
	public static final int MIN_DICE = 2;
	
	// A field for the result Property object
	private Property result = null;
	// The counter for determining how many dice to roll
	private int counter = 0;
	// Property values for each die
	private Property[] diceValues;
	
	/**
	 * This constructs a FNoise generator for the given property type, with the default dice.
	 * @param typeFlag The type of property this generator makes.
	 */
	public FNoise(PropertyType typeFlag){
		this(DEFAULT_DICE, typeFlag);
	}

	/**
	 * This constructs a FNoise generator for the given property type and number of default dice.
	 * @param numDice The number of dice to create this generator with.
	 * @param typeFlag The type of property this generator makes.
	 */
	public FNoise(int numDice, PropertyType typeFlag){
		// Bounds check the number of dice
		if(numDice > MAX_DICE)
			numDice = MAX_DICE;
		else if(numDice < 1)
			numDice = 1;
		diceValues = new Property[numDice];
		// Set the current value
		switch(typeFlag){
			case DURATION:			result = new Time();
									break;
			case SPACING:			result = new Time();
									break;
			case VELOCITY:			result = new Velocity();
									break;
			case MICRO_ORG:			result = new Organization(OrgMode.MICRO);
									break;
			case MACRO_ORG:			result = new Organization(OrgMode.MACRO);
									break;
			default:				throw new IllegalArgumentException("TYPE ID NOT RECOGNIZED");
		}
		result.randomize();
		// Setup dice values
		for(int i = 0 ; i < diceValues.length ; i++){
			switch(typeFlag){
				case DURATION:			diceValues[i] = new Time();
										break;
				case SPACING:			diceValues[i] = new Time();
										break;
				case VELOCITY:			diceValues[i] = new Velocity();
										break;
				case MICRO_ORG:			diceValues[i] = new Organization(OrgMode.MICRO);
										break;
				case MACRO_ORG:			diceValues[i] = new Organization(OrgMode.MACRO);
										break;
				default:				throw new IllegalArgumentException("TYPE ID NOT RECOGNIZED");
			}
			diceValues[i].randomize();
		}
	}
	
	/**
	 * This gets the current number of dice set for this generator.
	 * @return The number of dice this generator has.
	 */
	public int getNumDice(){
		return diceValues.length;
	}
	
	/* (non-Javadoc)
	 * @see generators.Generator#getResult()
	 */
	@Override
	public Property getResult(){
		return result;
	}

	/* (non-Javadoc)
	 * @see generators.Generator#step()
	 */
	@Override
	public void step(){
		// Store the current counter and increment it
		int oldCounter = counter;
		counter++;
		// Create a mask for checking each bit
		int mask = 1;
		for(int i = 0 ; i < diceValues.length ; i++){
			// Check the bit of the old counter and the current one
			int oldMasked = oldCounter & mask;
			int currentMasked = counter & mask;
			// If they are not equal, the bit has changed
			if(oldMasked != currentMasked)
				diceValues[i].randomize();;
			mask <<= 1;
		}
		// Average all the dice values and set the result to the average
		int avgDelta = 0;
		for(int i = 0 ; i < diceValues.length ; i++)
			avgDelta += diceValues[i].getValue();
		avgDelta /= diceValues.length;
		result.setValueToClosest(avgDelta);
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
