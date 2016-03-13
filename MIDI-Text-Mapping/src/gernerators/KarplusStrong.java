package gernerators;

import java.util.Arrays;

import gernerators.properties.Organization;
import gernerators.properties.Organization.OrgMode;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;

/**
 * This class implements a generator that models the Karplus-Strong compositional algorithm.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class KarplusStrong implements Generator {
	
	/** A constant for the default buffer length */
	public static final int DEFAULT_BUFFER_LENGTH = 10;
	/** A constant for the default reset threshold */
	public static final int DEFAULT_THRESHOLD = 20;
	/** A constant for the maximum supported buffer size */
	public static final int MAX_BUFFER_SIZE = 30;
	/** A constant for the maximum threshold size */
	public static final int MAX_THRESHOLD = 70;
	/** A constant for the minimum buffer size */
	public static final int MIN_BUFFER_SIZE = 5;
	/** A constant for the minimum threshold size */
	public static final int MIN_THRESHOLD = 5;
	
	// The reset threshold for this instance
	private int resetThreshold;
	// A counter for counting how many times we have iterated over the buffer
	private int counter = 0;
	// Out current index in the buffer
	private int currentIndex = 0;
	// The buffer of Property objects
	private Property[] buffer;
	
	/**
	 * This constructs a Karplus-Strong generator for the given property.
	 * This constructor will use the default buffer length and threshold.
	 * @param typeFlag The type of property this generator will make.
	 */
	public KarplusStrong(PropertyType typeFlag){
		this(DEFAULT_BUFFER_LENGTH, DEFAULT_THRESHOLD, typeFlag);
	}
	
	/**
	 * This constructs a Karplus-Strong generator for the given property,
	 * using the given buffer length and threshold.
	 * @param bufferLength How many items should be in the buffer.
	 * @param resetThreshold How many times should we iterate through the buffer before resetting it.
	 * @param typeFlag The type of property this generator will make.
	 */
	public KarplusStrong(int bufferLength, int resetThreshold, PropertyType typeFlag){
		// Set fields and build buffer
		this.resetThreshold = resetThreshold;
		buffer = new Property[bufferLength];
		for(int i = 0 ; i < bufferLength ; i++){
			switch(typeFlag){
				case DURATION:		buffer[i] = new Time();
									break;
				case SPACING:		buffer[i] = new Time();
									break;
				case VELOCITY: 		buffer[i] = new Velocity();
									break;
				case MICRO_ORG:		buffer[i] = new Organization(OrgMode.MICRO);
									break;
				case MACRO_ORG:		buffer[i] = new Organization(OrgMode.MACRO);
									break;				
				default:			throw new IllegalArgumentException("TYPE ID NOT RECOGNIZED");
			}
		}
		// Randomize the buffer
		resetBuffer();
	}
	
	/**
	 * This method will reset/randomize the buffer and reset the counter.
	 */
	public void resetBuffer(){
		counter = 0;
		for(int i = 0 ; i < buffer.length ; i++)
			buffer[i].randomize();;
	}
	
	/**
	 * This method will determine if the counter is past a given reset threshold.
	 * @param threshold The threshold to check the counter against.
	 * @return True if the counter has past the given threshold.
	 */
	public boolean isPastThreshold(int threshold){
		return (counter > threshold);
	}
	
	// -----------------------------------------------------------------
	// Override Methods
	// -----------------------------------------------------------------

	/* (non-Javadoc)
	 * @see generators.Generator#getResult()
	 */
	@Override
	public Property getResult() {
		return buffer[currentIndex];
	}

	/* (non-Javadoc)
	 * @see generators.Generator#step()
	 */
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

	/* (non-Javadoc)
	 * @see generators.Generator#getNext()
	 */
	@Override
	public Property getNext() {
		step();
		return getResult();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String build = "Buffer conents:\n";
		build += Arrays.toString(buffer) + "\n";
		return build;
	}

}
