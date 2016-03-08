package gernerators.properties;

import java.util.Random;

public class Velocity extends Property {
	
	public static final int MAX_VELOCITY = 127;
	public static final int DEFAULT_VELOCITY = 96;
	public static final int MIN_VELOCITY = 36;
	private Random r = new Random();
	
	public Velocity(){
		this(DEFAULT_VELOCITY);
	}

	public Velocity(int value) {
		super(value);
		if((value > MAX_VELOCITY) || (value < MIN_VELOCITY))
			super.value = DEFAULT_VELOCITY;
	}

	@Override
	public void randomize() {
		value = r.nextInt(MAX_VELOCITY - MIN_VELOCITY + 1) + MIN_VELOCITY;
	}

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
