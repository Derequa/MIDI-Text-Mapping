package gernerators.mappers;

import gernerators.*;

public class TimeMapper {
	/**
	 * Determine a range of spacings, and map them to markov states
	 * FNoise values, etc. Then test MIDI file mapping and generation before mappign velocity, duration and org methods
	 */
	
	public static final int TICKS_PER_QUARTER = music.MidiFile.TICKS_PER_BEAT;
	public static final int TICKS_PER_HALF = 2 * TICKS_PER_QUARTER;
	public static final int TICKS_PER_WHOLE = 2 * TICKS_PER_HALF;
	public static final int TICKS_PER_EIGTH = TICKS_PER_QUARTER / 2;
	public static final int TICKS_PER_SIXTEENTH = TICKS_PER_EIGTH / 2;
	
	private Generator g;
	private int currentTime = 0;
	private int maxTime;
	
	public TimeMapper(Generator g, int maxTime){
		this.g = g;
	}
	
	public int stepTime(){
		int nextTime = currentTime;
		
		if(g instanceof FNoise){
			
		}
		else if(g instanceof KarplusStrong){
			
		}
		else if(g instanceof Markov){
			
		}
		else if(g instanceof TriangularDist){
			
		}
		
		return currentTime;
	}
	
	public int getCurrentTime(){
		return currentTime;
	}
}
