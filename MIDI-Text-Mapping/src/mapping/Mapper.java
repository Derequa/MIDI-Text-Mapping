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

/**
 * This class handles mapping bytes from a file to MIDI notes.
 * It contains methods for setting up a mapping scheme, writing to file,
 * and processing notes.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Mapper {
	
	// For time-stamping notes in the mapping process and checking how far we are
	private int ticksSoFar = 0;
	// A list of all the note objects mapped from the file
	private LinkedList<Note> mappedNotes = new LinkedList<Note>();
	// A map that links bytes to a set of MIDI note values
	private HashMap<Integer, LinkedList<Integer>> mappingScheme = new HashMap<Integer, LinkedList<Integer>>();
	
	/** The Settings object that defines how this Mapper will behave */
	public Settings settings = new Settings();
	
	/**
	 * This constructs a Mapper object according to the given settings.
	 * @param s The settings for this Mapper.
	 */
	public Mapper(Settings s) {
		settings = s;
	}
	
	/**
	 * This will import the mapping scheme defined in the Settings object of this Mapper.
	 * WARNING: Importing a mapping scheme will clear and replace the current one.
	 * @throws Exception If the file cannot be opened or has an invalid format.
	 */
	public void importMappingScheme() throws Exception{
		if(settings.filemode)
			importMappingScheme(settings.mappingScheme);
	}
	
	/**
	 * This will import the mapping scheme defined by the given file.
	 * WARNING: Importing a mapping scheme will clear and replace the current one.
	 * @param f The file containing the mapping scheme to read in.
	 * @throws Exception If the file cannot be opened or has an invalid format.
	 */
	public void importMappingScheme(File f) throws Exception{
		if(!settings.filemode)
			return;
		try{
			// If null was give, read in the default mapping string
			if(f == null)
				importMappingScheme(new Scanner(DefaultMaps.defaultMapping));
			// Try to open a scanner to read in the mapping scheme
			else
				importMappingScheme(new Scanner(f));
		} catch (FileNotFoundException e) {
			Settings.failAndHalt("UNABLE TO OPEN MAPPING FILE!");
			e.printStackTrace();
		} catch (Exception e) {
			Settings.failAndHalt("UNABLE TO PROCESS MAPPING FILE!");
			e.printStackTrace();
		}
	}
	
	/**
	 * This maps the file defined in the Settings object for this Mapper.
	 */
	public void mapFile(){
		if(settings.filemode)
			mapFile(settings.getFileToMap());
	}

	/**
	 * This maps the given file (or directory) according to the scheme to note objects.
	 * @param The File object to map.
	 */
	public void mapFile(File f){
		if(!settings.filemode)
			return;
		Settings.statusMessage("READING FILE...");
		// Check for null
		if(f == null){
			Settings.fail("NULL FILE GIVEN!");
			throw new NullPointerException("NULL FILE GIVEN!");
		}
		// Make recursive calls if the file is a directory
		if(f.isDirectory()){
			for(File subFile: f.listFiles())
				mapFile(subFile);
			return;
		}
		
		// Initialize DataStream for reading bytes
		DataInputStream fileInput = null;
		try {
			fileInput = new DataInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try{
			// Loop through all the available bytes
			for(byte b = fileInput.readByte(); ; b = fileInput.readByte()){
				// Wrap the byte as an Integer
				Integer wrappedByte = new Integer(b);
				// Updates the current tick
				ticksSoFar += settings.getSpacingGenerator().getNext().getValue();
				// Stop if we are past the length
				if(settings.getLength() < ticksSoFar)
					return;
				// If this byte is not mapped, move one
				if(!mappingScheme.containsKey(wrappedByte))
					continue;
				// Create the note objects mapped to this byte
				for(Integer i : mappingScheme.get(wrappedByte)){
					mappedNotes.add(new Note(i.intValue(), settings.getVelocityGenerator().getResult().getValue(),
									ticksSoFar, settings.getDurationGenerator().getResult().getValue()));
				}
				// Step the generators
				settings.getVelocityGenerator().step();
				settings.getDurationGenerator().step();
			}
		} catch (IOException e){
			Settings.statusMessage("DONE READING FILE!");
		}
	}
	
	/**
	 * This method applies the given organization 
	 * generators to the mapped Note objects.
	 */
	public void organize(){
		Collections.sort(mappedNotes);
		Settings.statusMessage("ORGANIZING FILE...");
		// High-Level organization call, if there is a given Generator
		if(settings.getMacroOrgGenerator() != null)
			organize(settings.getMacroOrgGenerator(), settings.getMacroThreshold());
		// Low-Level organization call, if there is a given Generator
		if(settings.getMicroOrgGenerator() != null)
			organize(settings.getMicroOrgGenerator(), settings.getMicroThreshold());
		Settings.statusMessage("DONE ORGANIZING FILE!");
	}
	
	/**
	 * This will write the current set of Note objects to the output file 
	 * defined in the given Settings object.
	 * @throws Exception If there is an error writing the file.
	 */
	public void writeToFile() throws Exception{
		writeToFile(settings.outputFile.getAbsolutePath());
	}
	
	/**
	 * This will write out the current set of Note objects to a file of the given filename.
	 * @param filename The name of the file to create.
	 * @throws Exception If there is an error writing the file.
	 */
	public void writeToFile(String filename) throws Exception{
		Settings.statusMessage("WRITING TO FILE...");
		// Create a MidiFile to start writing notes to
		MidiFile mFile = new MidiFile(filename, settings.getTempo());
		for(Note n : mappedNotes){
			// Print the notes we are writing only if in debug mode
			Settings.debugMessage(n.toString());
			mFile.playNote(n);
		}
		// Write the file to disk
		mFile.finish();
		Settings.statusMessage("DONE WRITING TO FILE!");
	}
	
	/**
	 * This will clear all the generated notes mapped from the input file.
	 */
	public void clearMappedNotes(){
		mappedNotes.clear();
	}
	
	/**
	 * Generate notes at the starting note until the tick in the settings object.
	 */
	public void generateNotes(){
		if(settings.filemode)
			return;
		int endTick = settings.getLength();
		if(endTick == Integer.MAX_VALUE)
			return;
		for(int counter = 0 ; counter < endTick ; counter += settings.getSpacingGenerator().getResult().getValue())
			mappedNotes.add(new Note(settings.getStartingNote(), settings.getVelocityGenerator().getNext().getValue(), counter, settings.getDurationGenerator().getNext().getValue()));
	}
	
	// -----------------------------------------------------------------
	// Private Helper Methods
	// -----------------------------------------------------------------
	
	/**
	 * This is a helper function to generalize reading a mapping file or string.
	 * @param s The scanner to get input from.
	 * @throws Exception If the format does not match the standard.
	 */
	private void importMappingScheme(Scanner s) throws Exception{
		Settings.statusMessage("IMPORTING MAPPING SCHEME...");
		// Clear mapping scheme
		mappingScheme.clear();
		while(s.hasNextLine()){
			// Set up line scanner and tokens
			Scanner lineScanner = new Scanner(s.nextLine());
			String value0 = null;
			String value1 = null;
			String noteValue = null;
			String temp = null;
			
			// Get the first token
			if(lineScanner.hasNext())
				value0 = lineScanner.next();
			// Get the second token and the third one if it's there
			if(lineScanner.hasNext()){
				temp = lineScanner.next();
				// A hyphen must be present for us to accept three tokens
				if(temp.equals("-")){
					if(lineScanner.hasNext())
						value1 = lineScanner.next();
					if(lineScanner.hasNext())
						noteValue = lineScanner.next();
				}
				else 
					noteValue = temp;
			}
			
			// If we do not have at least one value and note, fail
			if((value0 == null) || (noteValue == null)){
				lineScanner.close();
				s.close();
				Settings.fail("INVALID MAPPING FILE FORMAT!");
				throw new Exception("INVALID MAPPING FILE FORMAT!");
			}
			
			// Read-in and process the tokens as numbers
			int int_value0 = Integer.parseInt(value0);
			int int_noteValue = Integer.parseInt(noteValue);
			int_value0 = Math.abs(int_value0) % 256;
			int_noteValue = Math.abs(int_noteValue) % 128;
			
			// Check if two values were given
			if(value1 != null){
				// Read-in and process the second value
				int int_value1 = Integer.parseInt(value1);
				int_value1 = Math.abs(int_value1) % 256;
				
				// One value is supposed to be the start of a range, and the other is the end
				int start = int_value0;
				int end = int_value1;
				int d = 1;
				// Determine which direction we should step
				if(int_value0 > int_value1){
					start = int_value1;
					end = int_value0;
					d = -1;
				}
				
				// Step through the range
				for(int i = start ; i < end ; i += d){
					// If there is no mapping list for this value, setup a new one
					if(mappingScheme.get(new Integer(i)) == null)
						mappingScheme.put(new Integer(i), new LinkedList<Integer>());
					// Add the note to the mapping list
					mappingScheme.get(new Integer(i)).add(new Integer(int_noteValue));
				}
			}
			// If there is only one note to map, create a mapping list if it needs one, and then map the note
			else{
				if(mappingScheme.get(new Integer(int_value0)) == null)
					mappingScheme.put(new Integer(int_value0), new LinkedList<Integer>());
				mappingScheme.get(new Integer(int_value0)).add(new Integer(int_noteValue));
			}
			
			lineScanner.close();
		}
		s.close();
		Settings.statusMessage("DONE IMPORTING MAPPING SCHEME!");
	}
	
	/**
	 * This is a helper method to apply a more generic 
	 * organization generator to the mapped notes.
	 * @param g The Generator object that defines how to organize the notes.
	 * @param changeThreshold The beats to count in between stepping the generator.
	 */
	private void organize(Generator g, int changeThreshold){
		// This counts beats so we know when to step the generator
		float beatCounter = 0;
		// The most recent time we have encountered
		int newestTime = 0;
		for(Note n : mappedNotes){
			// If this note is more recent, update the beat counter and newest time
			if(n.getStartingTime() > newestTime){
				int lastTime = newestTime;
				newestTime = n.getStartingTime();
				beatCounter += (float) (newestTime - lastTime) / MidiFile.TICKS_PER_BEAT;
			}
			// If we are past the beat threshold, reset and step the generator
			if(beatCounter > changeThreshold){
				g.step();
				beatCounter = 0;
			}
			// Set the note value
			int delta = g.getResult().getValue();
			n.setNote(n.getNote() + delta);
		}
	}
}
