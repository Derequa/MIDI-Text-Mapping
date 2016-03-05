package gernerators;

import java.util.Random;

public class TriangularDist implements Generator {
	
	public static final int NORMAL = 0;
	public static final int LEFT_SLANT = 1;
	public static final int RIGHT_SLANT = 2;
	
	private int mode;
	private int range;
	private int lastResult;
	private Random r = new Random();
	
	public TriangularDist(int range){
		this(NORMAL, range);
	}
	
	public TriangularDist(int mode, int range){
		this.mode = mode;
		this.range = range;
		step();
	}
	
	public void changeMode(int newMode){
		if((newMode != NORMAL) && (newMode != LEFT_SLANT) && (newMode != RIGHT_SLANT))
			return;
		else
			mode = newMode;
	}
	
	public void changeRange(int newRange){
		if(newRange < 0)
			return;
		range = newRange;
	}

	@Override
	public int getResult() {
		return lastResult;
	}

	@Override
	public void step() {
		int num1 = r.nextInt(range + 1);
		int num2 = r.nextInt(range + 1);
		switch (mode){
			case NORMAL:		lastResult = ((num1 + num2) / 2);
								break;
			case LEFT_SLANT:	if(num1 < num2)
									lastResult = num1;
								else
									lastResult = num2;
								break;
			case RIGHT_SLANT:	if(num1 > num2)
									lastResult = num1;
								else
									lastResult = num2;
								break;
		}
	}

	@Override
	public int getNext() {
		step();
		return getResult();
	}

}
