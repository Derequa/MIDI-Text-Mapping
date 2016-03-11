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
import gernerators.Generator;

public class Mapper {
	
	private int ticksSoFar = 0;
	
	private LinkedList<Note> mappedNotes = new LinkedList<Note>();
	private HashMap<Integer, LinkedList<Integer>> mappingScheme = new HashMap<Integer, LinkedList<Integer>>();
	
	public Settings settings = new Settings();
	
	public Mapper(Settings s) {
		settings = s;
	}
	
	public void mapSetFile(){
		mapFile(settings.getFileToMap());
	}

	public void mapFile(File f){
		Settings.statusMessage("READING FILE...");
		if(f == null){
			Settings.fail("NULL FILE GIVEN!");
			throw new NullPointerException("NULL FILE GIVEN!");
		}
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
			Settings.statusMessage("DONE READING FILE!");
		}
	}
	
	public void organize(){
		Settings.statusMessage("ORGANIZING FILE...");
		if(settings.getMacroOrgGenerator() != null){
			Collections.sort(mappedNotes);
			organize(settings.getMacroOrgGenerator(), settings.getMacroThreshold());
		}
		if(settings.getMicroOrgGenerator() != null){
			Collections.sort(mappedNotes);
			organize(settings.getMicroOrgGenerator(), settings.getMicroThreshold());
		}
		Settings.statusMessage("DONE ORGANIZING FILE!");
	}
	
	private void organize(Generator g, int changeThreshold){
		int beatCounter = 0;
		int newestTime = -1;
		for(Note n : mappedNotes){
			if(n.getStartingTime() > newestTime){
				g.step();
				newestTime = n.getStartingTime();
				beatCounter++;
			}
			if(beatCounter >= changeThreshold){
				int delta = g.getResult().getValue();
				n.setNote(n.getNote() + delta);
				beatCounter = 0;
			}
		}
	}
	
	public void writeToFile(){
		writeToFile(settings.outputFile.getAbsolutePath());
	}
	
	public void writeToFile(String filename){
		Settings.statusMessage("WRITING TO FILE...");
		MidiFile mFile = new MidiFile(filename, settings.getTempo());
		Settings.debugMessage(mappedNotes.toString());
		for(Note n : mappedNotes)
			mFile.playNote(n);
		mFile.finish();
		Settings.statusMessage("DONE WRITING TO FILE!");
	}
	
	public void importMappingScheme(){
		importMappingScheme(settings.mappingScheme);
	}
	
	public void importMappingScheme(File f){
		try{
			if(f == null)
				importMappingScheme(new Scanner(DefaultMaps.defaultMapping));
			else
				importMappingScheme(new Scanner(f));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void importMappingScheme(Scanner s) throws Exception{
		Settings.statusMessage("IMPORTING MAPPING SCHEME...");
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
				Settings.fail("INVALID MAPPING FILE FORMAT!");
				throw new Exception("INVALID MAPPING FILE FORMAT!");
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
		Settings.statusMessage("DONE IMPORTING MAPPING SCHEME!");
	}
	
}
