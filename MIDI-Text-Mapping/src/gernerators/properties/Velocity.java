package gernerators.properties;

public class Velocity extends Property {
	
	public static final int MAX_VELOCITY = 127;
	public static final int DEFAULT_VELOCITY = 96;
	public static final int MIN_VELOCITY = 0;
	
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
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueToClosest(int newValue) {
		// TODO Auto-generated method stub

	}

}
