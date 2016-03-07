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
		Mapper m = new Mapper(null, GeneratorType.KARPLUS, GeneratorType.FNOISE, GeneratorType.KARPLUS, GeneratorType.FNOISE, GeneratorType.FNOISE);
		m.mapFile(new File("default.txt"));
		m.writeToFile(null);
	}
	
	/**
	 * Broken things:
	 * Markov for all properties
	 * velocity
	 * all organization
	 */
	
	public static void testMarkov(){
		System.out.println("Creating Markov Object...");
		Markov m = new Markov();
		System.out.println(m);
		System.out.println("\nCurrent State: " + m.getResult());
		for(int i = 0 ; i < 3 ; i++){
			System.out.println("Transitioning...");
			m.step();
			System.out.println("\nCurrent State: " + m.getResult());
		}
	}
	
	public static void testKarplus(){
		System.out.println("Creating Karplus Object...");
		KarplusStrong k = new KarplusStrong(10, 20, PropertyType.TIME);
		System.out.println(k);
		while(!k.isPastThreshold(10)){
			k.step();
			System.out.println(k);
		}
		k.step();
		System.out.println(k);
	}
	
	public static void testFNoise(){
		String build = "";
		System.out.println("Creating FNoise Object...");
		FNoise f = new FNoise(3, PropertyType.TIME);
		for(int c = 0 ; c < 3 ; c++){
			build += "[ ";
			for(int i = 0 ; i < 8 ; i++){
				build += f.getNext() + ", ";
			}
			build += " ]\n";
		}
		System.out.println(build);
	}
}
