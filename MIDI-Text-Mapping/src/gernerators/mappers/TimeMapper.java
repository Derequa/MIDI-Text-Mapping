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
		this.maxTime = maxTime;
	}
	
	public int stepTime(){
		int nextTime = 0;
		
		if(g instanceof FNoise){
			nextTime = mapFNoise((FNoise) g);
		}
		else if(g instanceof KarplusStrong){
			nextTime = mapKarplusStrong((KarplusStrong) g);
		}
		else if(g instanceof Markov){
			nextTime = mapMarkov((Markov) g);
		}
		else if(g instanceof TriangularDist){
			nextTime = mapTriangularDist((TriangularDist) g);
		}
		
		currentTime = nextTime;
		return currentTime;
	}
	
	public int getCurrentTime(){
		return currentTime;
	}
	
	private int mapFNoise(FNoise f){
		int maxRange = f.getNumDice() * 6;
		f.step();
		return 0;
	}
	
	private int mapKarplusStrong(KarplusStrong k){
		return 0;
	}
	
	private int mapMarkov(Markov m){
		return 0;
	}
	
	private int mapTriangularDist(TriangularDist t){
		return 0;
	}
}
