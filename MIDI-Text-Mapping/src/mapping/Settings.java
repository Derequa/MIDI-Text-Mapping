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

public class Settings {
	
	public static boolean debug = true;
	
	public static final int TEMPO_MAX = 330;
	public static final int TEMPO_MIN = 30;
	public static final int TEMPO_DEFAULT = 130;
	
	public static final int MACRO_ORG_MAX = 64;
	public static final int MACRO_ORG_DEFAULT = 16;
	public static final int MICRO_ORG_MAX = 16;
	public static final int MICRO_ORG_DEFAULT = 0;
	public static final int ORG_MIN = 0;
	
	public static PrintStream console = System.out;
	private File toMap;
	public File mappingScheme;
	public File outputFile;
	
	private int maxTick;
	private float time;
	private int tempo = TEMPO_DEFAULT;
	private int macroThreshold = MACRO_ORG_DEFAULT;
	private int microThreshold = MICRO_ORG_DEFAULT;
	
	private Generator macroOrg;
	private Generator microOrg;
	private Generator duration;
	private Generator velocity;
	private Generator spacing;
	
	public int getMacroThreshold(){
		return macroThreshold;
	}
	
	public int getMicroThreshold(){
		return microThreshold;
	}
	
	public File getFileToMap(){
		return toMap;
	}
	
	public int getTempo(){
		return tempo;
	}
	
	public int getLength(){
		return maxTick;
	}
	
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
	
	public Generator getMacroOrgGenerator(){
		return macroOrg;
	}
	
	public Generator getMicroOrgGenerator(){
		return microOrg;
	}
	
	public Generator getDurationGenerator(){
		return duration;
	}
	
	public Generator getVelocityGenerator(){
		return velocity;
	}
	
	public Generator getSpacingGenerator(){
		return spacing;
	}
	
	public void setFileToMap(File toMap) throws Exception{
		if(toMap == null){
			failAndHalt("NULL FILE GIVEN!");
		}
		this.toMap = toMap;
	}
	
	public void setMacroThreshold(int newThreshold){
		if((newThreshold > MACRO_ORG_MAX) || (newThreshold < ORG_MIN)){
			fail("INVALID MACRO ORGANIZATION THRESHOLD");
			return;
		}
		macroThreshold = newThreshold;
	}
	
	public void setMicroThreshold(int newThreshold){
		if((newThreshold > MICRO_ORG_MAX) || (newThreshold < ORG_MIN)){
			fail("INVALID MICRO ORGANIZATION THRESHOLD");
			return;
		}
		microThreshold = newThreshold;
	}
	
	public void setTempo(int newTempo){
		if((newTempo > TEMPO_MAX) || (newTempo < TEMPO_MIN)){
			fail("ERROR: INVALID TEMPO PARAMETER!");
			return;
		}
		tempo = newTempo;
		maxTick = (int) (tempo * MidiFile.TICKS_PER_BEAT * time);
	}
	
	public void setLength(float time){
		this.time = time;
		if(time < -1){
			fail("INVALID LENGTH PARAMETER!");
			this.time = -1;
		}
		if(time >= 0)
			maxTick = (int) (tempo * MidiFile.TICKS_PER_BEAT * time);
		else
			maxTick = Integer.MAX_VALUE;
	}

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
	
	
	
	public void setSpacing(Generator newSpacing){
		if(newSpacing == null)
			fail("NULL GENERATOR");
		spacing = newSpacing;
	}
	
	public void setDuration(Generator newDuration){
		if(newDuration == null)
			fail("NULL GENERATOR");
		duration = newDuration;
	}
	
	public void setVelocity(Generator newVelocity){
		if(newVelocity == null)
			fail("NULL GENERATOR");
		velocity = newVelocity;
	}
	
	public void setMicroOrg(Generator newMicroOrg){
		microOrg = newMicroOrg;
	}
	
	public void setMacroOrg(Generator newMacroOrg){
		macroOrg = newMacroOrg;
	}
	
	public static void fail(String message){
		console.println("ERROR: " + message);
	}
	
	public static void failAndHalt(String message) throws Exception{
		fail(message);
		fail("UNABLE TO CREATE FILE!");
		throw new Exception(message);
	}
	
	public static void statusMessage(String message){
		console.println("STATUS: " + message);
	}
	
	public static void debugMessage(String message){
		if(debug)
			console.println("DEBUG: " + message);
	}

}
