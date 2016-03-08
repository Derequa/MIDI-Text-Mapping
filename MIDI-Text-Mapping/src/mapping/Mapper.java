package mapping;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import gernerators.FNoise;
import gernerators.Generator;
import gernerators.Generator.GeneratorType;
import gernerators.KarplusStrong;
import gernerators.Markov;
import gernerators.TriangularDist;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Velocity;
import music.MidiFile;
import music.Note;
import music.Tester;

public class Mapper {
	
	private int ticksSoFar = 0;
	
	private LinkedList<Note> mappedNotes = new LinkedList<Note>();
	private HashMap<Integer, LinkedList<Integer>> mappingScheme = new HashMap<Integer, LinkedList<Integer>>();
	
	private Generator spacing;
	private Generator velocity;
	private Generator duration;
	private Generator microOrg;
	private Generator macroOrg;
	
	private String defaultMapping = "0 - 64 50\n"
								  + "0 - 64 67\n"
								  + "0 - 64 72\n"
								  + "65 - 127 55\n"
								  + "65 - 127 64\n"
								  + "65 - 127 67\n";
	
	public Mapper(File scheme, GeneratorType spacing, GeneratorType velocity, GeneratorType duration, GeneratorType microOrg, GeneratorType macroOrg){
		try{
			if(scheme == null)
				importMappingScheme(new Scanner(defaultMapping));
			else
				importMappingScheme(new Scanner(scheme));
		} catch (Exception e) {
			e.printStackTrace();
		}
		switchSpacing(spacing);
		switchVelocity(velocity);
		switchDuration(duration);
		switchMicroOrg(microOrg);
		switchMacroOrg(macroOrg);
	}
	
	public void mapFile(File f){
		if(f == null)
			throw new NullPointerException("Null file given!");
		if(f.isDirectory()){
			for(File subFile: f.listFiles())
				mapFile(subFile);
			return;
		}
		
		DataInputStream fileInput = null;
		try {
			fileInput = new DataInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try{ 
			for(byte b = fileInput.readByte() ; ; b = fileInput.readByte()){
				Integer wrappedByte = new Integer(b);
				ticksSoFar += spacing.getNext().getValue();
				if(!mappingScheme.containsKey(wrappedByte))
					continue;
				for(Integer i : mappingScheme.get(wrappedByte)){
					mappedNotes.add(new Note(i.intValue(), velocity.getResult().getValue(), ticksSoFar, duration.getResult().getValue()));
				}
				velocity.step();
				duration.step();
			}
		} catch (IOException e){
			if(Tester.debug)
				System.out.println("DONE READING FILE");
		}
	}
	
	public void organize(){
		if(microOrg == null)
			return;
		Collections.sort(mappedNotes);
		int newestTime = -1;
		for(Note n : mappedNotes){
			if(n.getStartingTime() > newestTime){
				microOrg.step();
				newestTime = n.getStartingTime();
			}
			int delta = microOrg.getResult().getValue();
			n.setNote(n.getNote() + delta);
		}
	}
	
	public void writeToFile(String filename){
		MidiFile mFile = new MidiFile(filename);
		if(Tester.debug)
			System.out.println(mappedNotes);
		for(Note n : mappedNotes)
			mFile.playNote(n);
		mFile.finish();
	}
	
	public void importMappingScheme(Scanner s) throws Exception{
		mappingScheme.clear();
		while(s.hasNextLine()){
			Scanner lineScanner = new Scanner(s.nextLine());
			String value0 = null;
			String value1 = null;
			String noteValue = null;
			String temp = null;
			if(lineScanner.hasNext())
				value0 = lineScanner.next();
			if(lineScanner.hasNext()){
				temp = lineScanner.next();
				if(temp.equals("-")){
					if(lineScanner.hasNext())
						value1 = lineScanner.next();
					if(lineScanner.hasNext())
						noteValue = lineScanner.next();
				}
				else 
					noteValue = temp;
			}
			
			if((value0 == null) || (noteValue == null)){
				lineScanner.close();
				s.close();
				throw new Exception("Invalid mapping file format!");
			}
			
			int int_value0 = Integer.parseInt(value0);
			int int_noteValue = Integer.parseInt(noteValue);
			
			int_value0 = Math.abs(int_value0) % 256;
			int_noteValue = Math.abs(int_noteValue) % 128;
			
			if(value1 != null){
				int int_value1 = Integer.parseInt(value1);
				int_value1 = Math.abs(int_value1) % 256;
				int start = int_value0;
				int end = int_value1;
				int d = 1;
				if(int_value0 > int_value1){
					start = int_value1;
					end = int_value0;
					d = -1;
				}
				for(int i = start ; i < end ; i += d){
					if(mappingScheme.get(new Integer(i)) == null){
						LinkedList<Integer> newList = new LinkedList<Integer>();
						newList.add(new Integer(int_noteValue));
						mappingScheme.put(new Integer(i), newList);
					}
					else
						mappingScheme.get(new Integer(i)).add(new Integer(int_noteValue));
				}
			}
			else{
				if(mappingScheme.get(new Integer(int_value0)) == null){
					LinkedList<Integer> newList = new LinkedList<Integer>();
					newList.add(int_noteValue);
					mappingScheme.put(new Integer(int_value0), newList);
				}
				else
					mappingScheme.get(new Integer(int_value0)).add(new Integer(int_noteValue));
			}
			
			lineScanner.close();
		}
		s.close();
	}
	
	public void switchSpacing(GeneratorType newType){
		switch(newType){
			case FNOISE:		spacing  = new FNoise(PropertyType.SPACING);
								break;
			case KARPLUS:		spacing = new KarplusStrong(PropertyType.SPACING);
								break;
			case TRIANGULAR:	spacing = new TriangularDist(PropertyType.SPACING);
								break;
			case MARKOV:		spacing = setupDefaultMarkov(PropertyType.SPACING);
								break;
			default:			throw new IllegalArgumentException("INVALID SPACING GENERATOR MODE");
		}
	}
	
	public void switchDuration(GeneratorType newType){
		switch(newType){
			case FNOISE:		duration  = new FNoise(PropertyType.DURATION);
								break;
			case KARPLUS:		duration = new KarplusStrong(PropertyType.DURATION);
								break;
			case TRIANGULAR:	duration = new TriangularDist(PropertyType.DURATION);
								break;
			case MARKOV:		duration = setupDefaultMarkov(PropertyType.DURATION);
								break;
			default:			throw new IllegalArgumentException("INVALID DURATION GENERATOR MODE");
		}
	}
	
	public void switchVelocity(GeneratorType newType){
		switch(newType){
			case FNOISE:		velocity = new FNoise(PropertyType.VELOCITY);
								break;
			case KARPLUS:		velocity = new KarplusStrong(PropertyType.VELOCITY);
								break;
			case TRIANGULAR:	velocity = new TriangularDist(PropertyType.VELOCITY);
								break;
			case MARKOV:		velocity = setupDefaultMarkov(PropertyType.VELOCITY);
								break;
			default:			throw new IllegalArgumentException("INVALID VELOCITY GENERATOR MODE");
		}
	}
	
	public void switchMicroOrg(GeneratorType newType){
		// TODO fix dis
		switch(newType){
			case FNOISE:		microOrg = new FNoise(PropertyType.MICRO_ORG);
								break;
			case NONE:			microOrg = null;
								break;
			case TRIANGULAR:	microOrg = new TriangularDist(PropertyType.MICRO_ORG);
								break;
			case MARKOV:		microOrg = setupDefaultMarkov(PropertyType.MICRO_ORG);
								break;
			default:			throw new IllegalArgumentException("INVALID MICRO ORGANIZATION GENERATOR MODE");
		}
	}
	
	public void switchMacroOrg(GeneratorType newType){
		// TODO fix dis
		switch(newType){
			case FNOISE:		macroOrg = new FNoise(PropertyType.MACRO_ORG);
								break;
			case NONE:			macroOrg = null;
								break;
			case TRIANGULAR:	macroOrg = new TriangularDist(PropertyType.MACRO_ORG);
								break;
			case MARKOV:		macroOrg = setupDefaultMarkov(PropertyType.MACRO_ORG);
								break;
			default:			throw new IllegalArgumentException("INVALID MACRO ORGANIZATION GENERATOR MODE");
		}
	}
	
	
	
	public void switchSpacing(Generator newSpacing){
		spacing = newSpacing;
	}
	
	public void switchDuration(Generator newDuration){
		duration = newDuration;
	}
	
	public void switchVelocity(Generator newVelocity){
		velocity = newVelocity;
	}
	
	public void switchMicroOrg(Generator newMicroOrg){
		microOrg = newMicroOrg;
	}
	
	public void switchMacroOrg(Generator newMacroOrg){
		macroOrg = newMacroOrg;
	}
	
	private Markov setupDefaultMarkov(PropertyType type){
		// TODO fix dis
		return null;
	}
}
