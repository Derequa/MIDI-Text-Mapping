package gernerators;

import java.util.Random;

import gernerators.properties.Property;
import gernerators.properties.Time;

public class TriangularDist implements Generator {
	
	public static final int NORMAL = 0;
	public static final int LEFT_SLANT = 1;
	public static final int RIGHT_SLANT = 2;
	
	private int mode;
	private Property lastResult;
	private Property worker0;
	private Property worker1;
	
	public TriangularDist(int typeFlag){
		this(NORMAL, typeFlag);
	}
	
	public TriangularDist(int mode, int typeFlag){
		this.mode = mode;
		switch(typeFlag){
			case Property.ID_TIME:		lastResult = new Time();
										worker0 = new Time();
										worker1 = new Time();
										break;
			case Property.ID_VELOCITY:	//
										break;
			default:					throw new IllegalArgumentException("TYPE ID NOT RECOGNIZED");
		}
		// TODO Add support for more types
		step();
	}
	
	public void changeMode(int newMode){
		if((newMode != NORMAL) && (newMode != LEFT_SLANT) && (newMode != RIGHT_SLANT))
			return;
		else
			mode = newMode;
	}

	@Override
	public Property getResult() {
		return lastResult;
	}

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

	@Override
	public Property getNext() {
		step();
		return getResult();
	}

	public int getMode(){
		return mode;
	}
}
