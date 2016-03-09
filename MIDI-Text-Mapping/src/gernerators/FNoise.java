package gernerators;

import gernerators.properties.Organization;
import gernerators.properties.Organization.OrgMode;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;

public class FNoise implements Generator {
	
	public static final int DEFAULT_DICE = 4;
	public static final int MAX_DICE = 32;
	public static final int MIN_DICE = 2;
	private Property result = null;
	private int counter = 0;
	private Property[] diceValues;
	
	public FNoise(PropertyType typeFlag){
		this(DEFAULT_DICE, typeFlag);
	}

	public FNoise(int numDice, PropertyType typeFlag){
		if(numDice > MAX_DICE)
			numDice = MAX_DICE;
		else if(numDice < 1)
			numDice = 1;
		diceValues = new Property[numDice];
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
			// TODO Add other types
		}
	}
	
	public Property getResult(){
		return result;
	}

	public void step(){
		int oldCounter = counter;
		counter++;
		int mask = 1;
		for(int i = 0 ; i < diceValues.length ; i++){
			int oldMasked = oldCounter & mask;
			int currentMasked = counter & mask;
			if(oldMasked != currentMasked)
				diceValues[i].randomize();;
			mask <<= 1;
		}
		int avgDelta = 0;
		for(int i = 0 ; i < diceValues.length ; i++)
			avgDelta += diceValues[i].getValue();
		avgDelta /= diceValues.length;
		result.setValueToClosest(avgDelta);
	}

	@Override
	public Property getNext() {
		step();
		return getResult();
	}
	
	public int getNumDice(){
		return diceValues.length;
	}
}
