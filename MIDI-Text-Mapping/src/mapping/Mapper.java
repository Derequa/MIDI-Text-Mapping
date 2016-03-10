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

import frontside.Tester;
import gernerators.FNoise;
import gernerators.Generator;
import gernerators.Generator.GeneratorType;
import gernerators.KarplusStrong;
import gernerators.Markov;
import gernerators.TriangularDist;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Velocity;

public class Mapper {
	
	private int ticksSoFar = 0;
	
	private LinkedList<Note> mappedNotes = new LinkedList<Note>();
	private HashMap<Integer, LinkedList<Integer>> mappingScheme = new HashMap<Integer, LinkedList<Integer>>();
	
	public Settings settings = new Settings();
	
	private String defaultMapping = "0 - 64 50\n"
								  + "0 - 64 67\n"
								  + "0 - 64 72\n"
								  + "65 - 127 55\n"
								  + "65 - 127 64\n"
								  + "65 - 127 67\n";
	
	public Mapper(Settings s) {
		settings = s;
	}
	
	public void mapSetFile(){
		mapFile(settings.getFileToMap());
	}

	public void mapFile(File f){
		if(settings.DEBUG)
			settings.console.println("STATUS: READING FILE...");
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
				ticksSoFar += settings.getSpacingGenerator().getNext().getValue();
				if(settings.getLength() < ticksSoFar)
					return;
				if(!mappingScheme.containsKey(wrappedByte))
					continue;
				for(Integer i : mappingScheme.get(wrappedByte)){
					mappedNotes.add(new Note(i.intValue(), settings.getVelocityGenerator().getResult().getValue(),
									ticksSoFar, settings.getDurationGenerator().getResult().getValue()));
				}
				settings.getVelocityGenerator().step();
				settings.getDurationGenerator().step();
			}
		} catch (IOException e){
			if(settings.DEBUG)
				settings.console.println("STATUS: DONE READING FILE!");
		}
	}
	
	public void organize(){
		if(settings.DEBUG)
			settings.console.println("STATUS: ORGANIZING FILE...");
		if(settings.getMicroOrgGenerator() == null)
			return;
		Collections.sort(mappedNotes);
		int newestTime = -1;
		for(Note n : mappedNotes){
			if(n.getStartingTime() > newestTime){
				settings.getMicroOrgGenerator().step();
				newestTime = n.getStartingTime();
			}
			int delta = settings.getMicroOrgGenerator().getResult().getValue();
			n.setNote(n.getNote() + delta);
		}
		if(settings.DEBUG)
			settings.console.println("STATUS: DONE ORGANIZING FILE!");
	}
	
	public void writeToFile(){
		writeToFile(settings.outputFile.getAbsolutePath());
	}
	
	public void writeToFile(String filename){
		if(settings.DEBUG)
			settings.console.println("STATUS: WRITING TO FILE...");
		MidiFile mFile = new MidiFile(filename, settings.getTempo());
		if(settings.DEBUG)
			settings.console.println(mappedNotes);
		for(Note n : mappedNotes)
			mFile.playNote(n);
		mFile.finish();
		if(settings.DEBUG)
			settings.console.println("STATUS: DONE WRITING TO FILE!");
	}
	
	public void importMappingScheme(){
		importMappingScheme(settings.mappingScheme);
	}
	
	public void importMappingScheme(File f){
		try{
			if(f == null)
				importMappingScheme(new Scanner(defaultMapping));
			else
				importMappingScheme(new Scanner(f));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void importMappingScheme(Scanner s) throws Exception{
		if(settings.DEBUG)
			settings.console.println("STATUS: IMPORTING MAPPING SCHEME...");
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
		if(settings.DEBUG)
			settings.console.println("STATUS: DONE IMPORTING MAPPING SCHEME!");
	}
	
}
