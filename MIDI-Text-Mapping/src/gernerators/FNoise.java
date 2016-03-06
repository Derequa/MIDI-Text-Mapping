package gernerators;

import java.util.Random;

import gernerators.properties.Property;
import gernerators.properties.Time;

public class FNoise implements Generator {
	
	private Property result = null;
	private int counter = 0;
	private Property[] diceValues;
	private Random roller = new Random();

	public FNoise(int numDice, int typeFlag){
		if(numDice > 32)
			numDice = 32;
		else if(numDice < 1)
			numDice = 1;
		diceValues = new Property[numDice];
		for(int i = 0 ; i < diceValues.length ; i++){
			switch(typeFlag){
			case Property.ID_TIME:			diceValues[i] = new Time();
											break;
			case Property.ID_VELOCITY:		//
											break;
			default:						throw new IllegalArgumentException("TYPE ID NOT RECOGNIZED");
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
