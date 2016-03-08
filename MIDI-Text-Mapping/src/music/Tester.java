package music;

import java.io.File;

import gernerators.FNoise;
import gernerators.KarplusStrong;
import gernerators.Markov;
import gernerators.Generator.GeneratorType;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import mapping.Mapper;

public class Tester {
	
	public static final boolean debug = true;
	
	public static void main(String[] args){
		Mapper m = new Mapper(null, GeneratorType.KARPLUS, GeneratorType.KARPLUS, GeneratorType.FNOISE, GeneratorType.TRIANGULAR, GeneratorType.FNOISE);
		m.mapFile(new File("default.txt"));
		m.organize();
		m.writeToFile(null);
	}
	
	/**
	 * Broken things:
	 * Markov for all properties
	 * velocity
	 * all organization
	 * 
	 * 
	 * Figure out timers for organization generation
	 */
}
