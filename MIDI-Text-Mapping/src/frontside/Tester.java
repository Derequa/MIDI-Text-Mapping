package frontside;

import java.io.File;
import java.io.PrintStream;

import gernerators.FNoise;
import gernerators.KarplusStrong;
import gernerators.Markov;
import gernerators.Generator.GeneratorType;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import mapping.Mapper;
import mapping.Settings;

public class Tester {
	
	public static void main(String[] args){
		FrontEnd d =  new FrontEnd();
		Settings.console = new PrintStream(new JTextOutputStream(d.gui_console));
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
