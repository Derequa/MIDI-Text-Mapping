package gernerators;

import java.util.Arrays;
import java.util.Random;

import gernerators.properties.Property;
import gernerators.properties.Time;

public class KarplusStrong implements Generator {
	
	private int resetThreshold = 10;
	private int counter = 0;
	private int currentIndex = 0;
	private Property[] buffer;
	
	public KarplusStrong(int bufferLength, int resetThreshold, int typeFlag){
		this.resetThreshold = resetThreshold;
		buffer = new Property[bufferLength];
		for(int i = 0 ; i < bufferLength ; i++){
			switch(typeFlag){
				case Property.ID_TIME:		buffer[i] = new Time();
											break;
				case Property.ID_VELOCITY: 	// TODO Add stuff
											break;
				default:					throw new IllegalArgumentException("TYPE ID NOT RECOGNIZED");
			}
		}
		resetBuffer();
	}

	@Override
	public Property getResult() {
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
		buffer[currentIndex].setValueToClosest((buffer[currentIndex].getValue() + buffer[nextIndex].getValue()) / 2);
		currentIndex = nextIndex;
	}

	@Override
	public Property getNext() {
		step();
		return getResult();
	}
	
	public void resetBuffer(){
		counter = 0;
		for(int i = 0 ; i < buffer.length ; i++)
			buffer[i].randomize();;
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
