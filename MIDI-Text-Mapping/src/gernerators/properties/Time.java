package gernerators.properties;

import java.util.Random;

/**
 * This class implements a time-based property to be used with a generator.
 * This can be used with a generator to create note durations and spacings.
 * @author Derek Batts - dsabtts@ncsu.edu
 *
 */
public class Time extends Property implements Comparable<Property> {
	
	/** A constant for a no tick value */
	public static final int NO_TICK = 0;
	/** A constant for a quarter-note tick value */
	public static final int TICKS_PER_QUARTER = mapping.MidiFile.TICKS_PER_BEAT;
	/** A constant for a half-note tick value */
	public static final int TICKS_PER_HALF = 2 * TICKS_PER_QUARTER;
	/** A constant for a whole-note tick value */
	public static final int TICKS_PER_WHOLE = 2 * TICKS_PER_HALF;
	/** A constant for a eigth-note tick value */
	public static final int TICKS_PER_EIGTH = TICKS_PER_QUARTER / 2;
	/** A constant for a sixteenth-note tick value */
	public static final int TICKS_PER_SIXTEENTH = TICKS_PER_EIGTH / 2;
	/** A constant array of all the constant times */
	public static final int[] TIMINGS = {TICKS_PER_SIXTEENTH,
										 TICKS_PER_EIGTH,
										 TICKS_PER_QUARTER,
										 TICKS_PER_HALF,
										 TICKS_PER_WHOLE,
										 NO_TICK};
	/** Index in the timing array for a eigth-note tick value */
	public static final int EIGTH_INDEX = 0;
	/** Index in the timing array for a sixteenth-note tick value */
	public static final int SIXTEENTH_INDEX = 1;
	/** Index in the timing array for a quarter-note tick value */
	public static final int QUARTER_INDEX = 2;
	/** Index in the timing array for a half-note tick value */
	public static final int HALF_INDEX = 3;
	/** Index in the timing array for a whole-note tick value */
	public static final int WHOLE_INDEX = 4;
	/** Index in the timing array for a no tick value */
	public static final int NO_TICK_INDEX = 5;
	
	// The RNG for this property instance
	private static Random r = new Random(System.nanoTime());
	
	/**
	 * This constructs a Time property with the default value of 0.
	 */
	public Time(){
		super(0);
		randomize();
	}
	
	// -----------------------------------------------------------------
	// Overriden Methods
	// -----------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see generators.properties.Property#randomize()
	 */
	@Override
	public void randomize(){
		setValueToClosest(r.nextInt(TICKS_PER_WHOLE + 1));
	}
	
	/* (non-Javadoc)
	 * @see generators.properties.Property#setValueToClosest(int)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(generators.properties.Time)
	 */
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
