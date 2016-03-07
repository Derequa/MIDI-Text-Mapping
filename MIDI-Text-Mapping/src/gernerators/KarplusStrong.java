package gernerators;

import java.util.Arrays;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;

public class KarplusStrong implements Generator {
	
	public static final int DEFAULT_BUFFER_LENGTH = 10;
	public static final int DEFAULT_THRESHOLD = 20;
	
	private int resetThreshold = 10;
	private int counter = 0;
	private int currentIndex = 0;
	private Property[] buffer;
	
	public KarplusStrong(PropertyType typeFlag){
		this(DEFAULT_BUFFER_LENGTH, DEFAULT_THRESHOLD, typeFlag);
	}
	
	public KarplusStrong(int bufferLength, int resetThreshold, PropertyType typeFlag){
		this.resetThreshold = resetThreshold;
		buffer = new Property[bufferLength];
		for(int i = 0 ; i < bufferLength ; i++){
			switch(typeFlag){
				case TIME:		buffer[i] = new Time();
											break;
				case VELOCITY: 	// TODO Add stuff
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
