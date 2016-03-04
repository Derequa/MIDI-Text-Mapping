package gernerators;

import java.util.Arrays;
import java.util.Random;

public class KarplusStrong implements Generator {
	
	private Random r = new Random();
	private int range;
	private int resetThreshold = 10;
	private int counter = 0;
	private int currentIndex = 0;
	private int[] buffer;
	
	public KarplusStrong(int bufferLength, int range, int resetThreshold){
		this.range = range;
		this.resetThreshold = resetThreshold;
		buffer = new int[bufferLength];
		resetBuffer();
	}

	@Override
	public int getResult() {
		return buffer[currentIndex];
	}

	@Override
	public void step() {
		if(isPastThreshold(resetThreshold)){
			resetBuffer();
			return;
		}
		int nextIndex = 0;
		if(!(currentIndex == (buffer.length - 1)))
			nextIndex = currentIndex + 1;
		else
			counter++;
		buffer[currentIndex] = (buffer[currentIndex] + buffer[nextIndex]) / 2;
		currentIndex = nextIndex;
	}

	@Override
	public int getNext() {
		step();
		return getResult();
	}
	
	public void resetBuffer(){
		counter = 0;
		for(int i = 0 ; i < buffer.length ; i++)
			buffer[i] = r.nextInt(range + 1);
	}
	
	public boolean isPastThreshold(int threshold){
		return (counter > threshold);
	}
	
	public String toString(){
		String build = "Buffer conents:\n";
		build += Arrays.toString(buffer) + "\n";
		return build;
	}

}
