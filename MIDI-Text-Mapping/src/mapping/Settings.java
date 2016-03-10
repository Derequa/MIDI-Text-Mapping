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
	
	public final boolean DEBUG = true;
	
	public static final int TEMPO_MAX = 330;
	public static final int TEMPO_MIN = 30;
	public static final int TEMPO_DEFAULT = 130;
	
	public PrintStream console = System.out;
	private File toMap;
	public File mappingScheme;
	public File outputFile;
	
	private int maxTick;
	private float time;
	private int tempo = TEMPO_DEFAULT;
	
	private Generator macroOrg;
	private Generator microOrg;
	private Generator duration;
	private Generator velocity;
	private Generator spacing;
	
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
	
	public void setFileToMap(File toMap){
		if(toMap == null){
			console.println("ERROR: NULL FILE GIVEN!");
			throw new IllegalArgumentException("NULL FILE GIVEN");
		}
		this.toMap = toMap;
	}
	
	public void setTempo(int newTempo){
		if((newTempo > TEMPO_MAX) || (newTempo < TEMPO_MIN)){
			console.println("ERROR: INVALID TEMPO PARAMETER!");
			throw new IllegalArgumentException("INVALID TEMPO PARAMETER");
		}
		tempo = newTempo;
		maxTick = (int) (tempo * MidiFile.TICKS_PER_BEAT * time);
	}
	
	public void setLength(float time){
		this.time = time;
		if(time < -1){
			console.println("ERROR: INVALID LENGTH PARAMETER!");
			throw new IllegalArgumentException("INVALID LENGTH PARAMETER");
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
			default:			console.println("ERROR: INVALID SPACING GENERATOR MODE!");
								throw new IllegalArgumentException("INVALID SPACING GENERATOR MODE");
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
			default:			console.println("ERROR: INVALID DURATION GENERATOR MODE!");
								throw new IllegalArgumentException("INVALID DURATION GENERATOR MODE");
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
			default:			console.println("ERROR: INVALID VELOCITY GENERATOR MODE!");
								throw new IllegalArgumentException("INVALID VELOCITY GENERATOR MODE");
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
			default:			console.println("ERROR: MICRO ORGANIZATION VELOCITY GENERATOR MODE!");
								throw new IllegalArgumentException("INVALID MICRO ORGANIZATION GENERATOR MODE");
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
			default:			console.println("ERROR: MACRO ORGANIZATION VELOCITY GENERATOR MODE!");
								throw new IllegalArgumentException("INVALID MACRO ORGANIZATION GENERATOR MODE");
		}
	}
	
	
	
	public void setSpacing(Generator newSpacing){
		if(newSpacing == null){
			console.println("ERROR: NULL GENERATOR!");
			throw new IllegalArgumentException("NULL GENERATOR");
		}
		spacing = newSpacing;
	}
	
	public void setDuration(Generator newDuration){
		if(newDuration == null){
			console.println("ERROR: NULL GENERATOR!");
			throw new IllegalArgumentException("NULL GENERATOR");
		}
		duration = newDuration;
	}
	
	public void setVelocity(Generator newVelocity){
		if(newVelocity == null){
			console.println("ERROR: NULL GENERATOR!");
			throw new IllegalArgumentException("NULL GENERATOR");
		}
		velocity = newVelocity;
	}
	
	public void setMicroOrg(Generator newMicroOrg){
		if(newMicroOrg == null){
			console.println("ERROR: NULL GENERATOR!");
			throw new IllegalArgumentException("NULL GENERATOR");
		}
		microOrg = newMicroOrg;
	}
	
	public void setMacroOrg(Generator newMacroOrg){
		if(newMacroOrg == null){
			console.println("ERROR: NULL GENERATOR!");
			throw new IllegalArgumentException("NULL GENERATOR");
		}
		macroOrg = newMacroOrg;
	}

}
