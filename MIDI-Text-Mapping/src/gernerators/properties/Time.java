package gernerators.properties;

import java.util.Random;

public class Time extends Property implements Comparable<Property> {
	
	public static final int NO_TICK = 0;
	public static final int TICKS_PER_QUARTER = music.MidiFile.TICKS_PER_BEAT;
	public static final int TICKS_PER_HALF = 2 * TICKS_PER_QUARTER;
	public static final int TICKS_PER_WHOLE = 2 * TICKS_PER_HALF;
	public static final int TICKS_PER_EIGTH = TICKS_PER_QUARTER / 2;
	public static final int TICKS_PER_SIXTEENTH = TICKS_PER_EIGTH / 2;
	
	private static final int[] TIMINGS = {TICKS_PER_SIXTEENTH,
										 TICKS_PER_EIGTH,
										 TICKS_PER_QUARTER,
										 TICKS_PER_HALF,
										 TICKS_PER_WHOLE,
										 NO_TICK};
	
	private static final int EIGTH_INDEX = 0;
	private static final int SIXTEENTH_INDEX = 1;
	private static final int QUARTER_INDEX = 2;
	private static final int HALF_INDEX = 3;
	private static final int WHOLE_INDEX = 4;
	private static final int NO_TICK_INDEX = 5;
	
	private static Random r = new Random(System.nanoTime());
	
	public Time(){
		super(0);
		randomize();
	}
	
	public void setValueToClosest(int newValue){
		int minDist = Integer.MAX_VALUE;
		int timingIndex = QUARTER_INDEX;
		for(int i = 0 ; i < TIMINGS.length ; i++){
			if(Math.abs(newValue - TIMINGS[i]) < minDist){
				timingIndex = i;
				minDist = Math.abs(newValue - TIMINGS[i]);
			}
		}
		value = TIMINGS[timingIndex];
	}
	
	public void randomize(){
		setValueToClosest(r.nextInt(TICKS_PER_WHOLE + 1));
	}

	@Override
	public int compareTo(Property arg0) {
		if(value == arg0.value)
			return 0;
		else if(value > arg0.value)
			return 1;
		else
			return -1;
	}
}
