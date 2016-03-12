package mapping;

import java.io.File;
import java.io.PrintStream;

import gernerators.FNoise;
import gernerators.Generator;
import gernerators.KarplusStrong;
import gernerators.Markov;
import gernerators.TriangularDist;
import gernerators.Generator.GeneratorType;
import gernerators.properties.Property.PropertyType;

/**
 * This class holds global stating settings, defaults and constants.
 * Instances of this object are used to define the behavior of Mapper objects.
 * This class also contains methods for printing errors, status messages, debug messages, and throwing generic exceptions.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Settings {
	
	/** This is a global debug flag for conditional execution and debug printing */
	public static boolean debug = true;
	
	/** A constant for the max value the tempo can be set to */
	public static final int TEMPO_MAX = 330;
	/** A constant for the lowest value the tempo can be set to */
	public static final int TEMPO_MIN = 30;
	/** A constant for the default value the tempo is set to */
	public static final int TEMPO_DEFAULT = 130;
	
	/** A constant for the max number of beats between macro-organization steps */
	public static final int MACRO_ORG_MAX = 64;
	/** A constant for the default number of beats between macro-organization steps */
	public static final int MACRO_ORG_DEFAULT = 16;
	/** A constant for the max number of beats between micro-organization steps */
	public static final int MICRO_ORG_MAX = 16;
	/** A constant for the default number of beats between micro-organization steps */
	public static final int MICRO_ORG_DEFAULT = 0;
	/** A constant for the minimum number of beats between organizational steps*/
	public static final int ORG_MIN = 0;
	
	
	/** A global PrintStream for all classes in this program to use */
	public static PrintStream console = System.out;
	
	// The file to process and map to notes
	private File toMap;
	// The file defining how to map bytes to notes
	public File mappingScheme;
	// The file to write to
	public File outputFile;
	
	// The maximum tick we can go to (length)
	private int maxTick;
	// The length we can go to (in minutes)
	private float time;
	// The tempo for this file
	private int tempo = TEMPO_DEFAULT;
	// Thresholds for stepping the organizers
	private int macroThreshold = MACRO_ORG_DEFAULT;
	private int microThreshold = MICRO_ORG_DEFAULT;
	
	// Generators for each property
	private Generator macroOrg;
	private Generator microOrg;
	private Generator duration;
	private Generator velocity;
	private Generator spacing;
	
	/**
	 * This will print a given error message to the current global PrintStream.
	 * @param message The error message to print.
	 */
	public static void fail(String message){
		console.println("ERROR: " + message);
	}
	
	/**
	 * This will print a given error message to the current global PrintStream, and
	 * throw a generic exception with the same message.
	 * @param message The error message to broadcast.
	 * @throws Exception A generic exception with the given message.
	 */
	public static void failAndHalt(String message) throws Exception{
		fail(message);
		throw new Exception(message);
	}
	
	/**
	 * This will print a status message to the global PrintStream.
	 * @param message The message to print.
	 */
	public static void statusMessage(String message){
		console.println("STATUS: " + message);
	}
	
	/**
	 * This will print a debug message to the global PrintStream.
	 * It will only be printed, if the global debug flag is true.
	 * @param message The debug message to print.
	 */
	public static void debugMessage(String message){
		if(debug)
			console.println("DEBUG: " + message);
	}
	
	// ---------------------------------------------------------------------
	// Getter Methods
	// ---------------------------------------------------------------------
	
	/**
	 * This gets the current threshold for macro-organizing.
	 * @return The current macro-organizing threshold (beats between steps)
	 */
	public int getMacroThreshold(){
		return macroThreshold;
	}
	
	/**
	 * This gets the current threshold for micro-organizing.
	 * @return The current micro-organizing threshold (beats between steps)
	 */
	public int getMicroThreshold(){
		return microThreshold;
	}
	
	/**
	 * This gets the file that bytes will be mapped from.
	 * @return The File object for the file we are mapping.
	 */
	public File getFileToMap(){
		return toMap;
	}
	
	/**
	 * This gets the tempo the of the file.
	 * @return The tempo of the file as an integer.
	 */
	public int getTempo(){
		return tempo;
	}
	
	/**
	 * This gets the length of the file in ticks.
	 * @return The length of the file in ticks.
	 */
	public int getLength(){
		return maxTick;
	}
	
	/**
	 * This method allows you to get the Generator object for a given property.
	 * @param mode The type of property you want to get a Generator for.
	 * @return The Generator object for the given property.
	 */
	public Generator getGenerator(PropertyType mode){
		switch(mode){
			case MACRO_ORG:		return getMacroOrgGenerator();
			case MICRO_ORG:		return getMicroOrgGenerator();
			case DURATION:		return getDurationGenerator();
			case VELOCITY:		return getVelocityGenerator();
			case SPACING:		return getSpacingGenerator();
			default:			return null;
		}
	}
	
	/**
	 * This gets the current macro-organization generator.
	 * @return The Generator object currently defined for macro-organization.
	 */
	public Generator getMacroOrgGenerator(){
		return macroOrg;
	}
	
	/**
	 * This gets the current micro-organization generator.
	 * @return The Generator object currently defined for micro-organization.
	 */
	public Generator getMicroOrgGenerator(){
		return microOrg;
	}
	
	/**
	 * This gets the current duration generator.
	 * @return The Generator object currently defined for duration.
	 */
	public Generator getDurationGenerator(){
		return duration;
	}
	
	/**
	 * This gets the current velocity generator.
	 * @return The Generator object currently defined for velocity.
	 */
	public Generator getVelocityGenerator(){
		return velocity;
	}
	
	/**
	 * This gets the current spacing generator.
	 * @return The Generator object currently defined for spacing.
	 */
	public Generator getSpacingGenerator(){
		return spacing;
	}
	
	// ---------------------------------------------------------------------
	// Setter Methods
	// ---------------------------------------------------------------------
	
	/**
	 * This sets a new file to map bytes from.
	 * @param toMap The file to map bytes from.
	 * @throws Exception If the file is null.
	 */
	public void setFileToMap(File toMap) throws Exception{
		if(toMap == null){
			failAndHalt("NULL FILE GIVEN!");
		}
		this.toMap = toMap;
	}
	
	/**
	 * This sets a new threshold for stepping macro-organization.
	 * @param newThreshold The new threshold to set.
	 */
	public void setMacroThreshold(int newThreshold){
		// Bounds check
		if((newThreshold > MACRO_ORG_MAX) || (newThreshold < ORG_MIN)){
			fail("INVALID MACRO ORGANIZATION THRESHOLD");
			return;
		}
		macroThreshold = newThreshold;
	}
	
	/**
	 * This sets a new threshold for stepping micro-organization.
	 * @param newThreshold The new threshold to set.
	 */
	public void setMicroThreshold(int newThreshold){
		// Bounds check
		if((newThreshold > MICRO_ORG_MAX) || (newThreshold < ORG_MIN)){
			fail("INVALID MICRO ORGANIZATION THRESHOLD");
			return;
		}
		microThreshold = newThreshold;
	}
	
	/**
	 * This set a new tempo for the file.
	 * @param newTempo The new tempo to set for the file.
	 */
	public void setTempo(int newTempo){
		// Bounds check
		if((newTempo > TEMPO_MAX) || (newTempo < TEMPO_MIN)){
			fail("ERROR: INVALID TEMPO PARAMETER!");
			return;
		}
		tempo = newTempo;
		// Update the max length based on the new tempo value
		if(time > -1)
			maxTick = (int) (tempo * MidiFile.TICKS_PER_BEAT * time);
	}
	
	/**
	 * This will set a new length for the file.
	 * A vaue of -1 will be interpreted as unlimited length.
	 * @param time The time in minutes to set the file length to.
	 */
	public void setLength(float time){
		this.time = time;
		// -1 is defines unlimited length
		if(time < -1){
			fail("INVALID LENGTH PARAMETER!");
			this.time = -1;
		}
		// Set the max tick according to time and tempo
		if(time >= 0)
			maxTick = (int) (tempo * MidiFile.TICKS_PER_BEAT * time);
		else
			maxTick = Integer.MAX_VALUE;
	}
	
	/**
	 * This allows you to set a new default Generator object for macro-organization,
	 * determined by the given type of generator.
	 * @param newType The type of generator to create (default mode).
	 */
	public void setMacroOrg(GeneratorType newType){
		switch(newType){
			case FNOISE:		macroOrg = new FNoise(PropertyType.MACRO_ORG);
								break;
			case NONE:			macroOrg = null;
								break;
			case TRIANGULAR:	macroOrg = new TriangularDist(PropertyType.MACRO_ORG);
								break;
			case MARKOV:		macroOrg = new Markov(PropertyType.MACRO_ORG);
								break;
			default:			fail("INVALID MACRO ORGANIZATION GENERATOR MODE!");
								break;
		}
	}
	
	/**
	 * This allows you to set a new default Generator object for micro-organization,
	 * determined by the given type of generator.
	 * @param newType The type of generator to create (default mode).
	 */
	public void setMicroOrg(GeneratorType newType){
		switch(newType){
			case FNOISE:		microOrg = new FNoise(PropertyType.MICRO_ORG);
								break;
			case NONE:			microOrg = null;
								break;
			case TRIANGULAR:	microOrg = new TriangularDist(PropertyType.MICRO_ORG);
								break;
			case MARKOV:		microOrg = new Markov(PropertyType.MICRO_ORG);
								break;
			default:			fail("INVALID MICRO ORGANIZATION GENERATOR MODE!");
								break;
		}
	}
	
	/**
	 * This allows you to set a new default Generator object for spacing,
	 * determined by the given type of generator.
	 * @param newType The type of generator to create (default mode).
	 */
	public void setDuration(GeneratorType newType){
		switch(newType){
			case FNOISE:		duration  = new FNoise(PropertyType.DURATION);
								break;
			case KARPLUS:		duration = new KarplusStrong(PropertyType.DURATION);
								break;
			case TRIANGULAR:	duration = new TriangularDist(PropertyType.DURATION);
								break;
			case MARKOV:		duration = new Markov(PropertyType.DURATION);
								break;
			default:			fail("INVALID DURATION GENERATOR MODE!");
								break;
		}
	}
	
	/**
	 * This allows you to set a new default Generator object for velocity,
	 * determined by the given type of generator.
	 * @param newType The type of generator to create (default mode).
	 */
	public void setVelocity(GeneratorType newType){
		switch(newType){
			case FNOISE:		velocity = new FNoise(PropertyType.VELOCITY);
								break;
			case KARPLUS:		velocity = new KarplusStrong(PropertyType.VELOCITY);
								break;
			case TRIANGULAR:	velocity = new TriangularDist(PropertyType.VELOCITY);
								break;
			case MARKOV:		velocity = new Markov(PropertyType.VELOCITY);
								break;
			default:			fail("INVALID VELOCITY GENERATOR MODE!");
								break;
		}
	}
	
	/**
	 * This allows you to set a new default Generator object for spacing,
	 * determined by the given type of generator.
	 * @param newType The type of generator to create (default mode).
	 */
	public void setSpacing(GeneratorType newType){
		switch(newType){
			case FNOISE:		spacing  = new FNoise(PropertyType.SPACING);
								break;
			case KARPLUS:		spacing = new KarplusStrong(PropertyType.SPACING);
								break;
			case TRIANGULAR:	spacing = new TriangularDist(PropertyType.SPACING);
								break;
			case MARKOV:		spacing = new Markov(PropertyType.SPACING);
								break;
			default:			fail("INVALID SPACING GENERATOR MODE!");
								break;
		}
	}
	
	/**
	 * This allows you to set a new Generator object for macro-organization,
	 * using the given Generator object.
	 * @param newMacroOrg The Generator to use for macro-organization.
	 */
	public void setMacroOrg(Generator newMacroOrg){
		macroOrg = newMacroOrg;
	}

	/**
	 * This allows you to set a new Generator object for micro-organization,
	 * using the given Generator object.
	 * @param newMacroOrg The Generator to use for micro-organization.
	 */
	public void setMicroOrg(Generator newMicroOrg){
		microOrg = newMicroOrg;
	}
	
	/**
	 * This allows you to set a new Generator object for duration,
	 * using the given Generator object.
	 * @param newMacroOrg The Generator to use for duration.
	 */
	public void setDuration(Generator newDuration){
		if(newDuration == null)
			fail("NULL GENERATOR");
		duration = newDuration;
	}
	
	/**
	 * This allows you to set a new Generator object for velocity,
	 * using the given Generator object.
	 * @param newMacroOrg The Generator to use for velocity.
	 */
	public void setVelocity(Generator newVelocity){
		if(newVelocity == null)
			fail("NULL GENERATOR");
		velocity = newVelocity;
	}
	
	/**
	 * This allows you to set a new Generator object for spacing,
	 * using the given Generator object.
	 * @param newMacroOrg The Generator to use for spacing.
	 */
	public void setSpacing(Generator newSpacing){
		if(newSpacing == null)
			fail("NULL GENERATOR");
		spacing = newSpacing;
	}

}
