package mapping;

import java.io.*;

import javax.sound.midi.*; // package for all midi classes

/**
 * MidiFile.java
 *
 * A very short program which builds and writes
 * a one-note Midi file. Modified to represent a simple
 * file object with methods for easily writing notes for
 * one instrument.
 *
 * @author  Karl Brown
 * last updated 2/24/2003
 * (Modified by Derek Batts - dsbatts@ncsu.edu)
 * Obtained from: http://www.automatic-pilot.com/midifile.html
 */
public class MidiFile {
	
	/** This is the number of MIDI ticks per quarter note or beat */
	public static final int TICKS_PER_BEAT = 96;
	
	// Track and Sequence
	private Track t0;
	private Sequence s;
	// The filename and tempo of this file
	private String filename;
	private int tempo;
	
	/**
	 * This will construct a MidiFile object with the defaults. 
	 * filename: outpit.mid 
	 * tempo: The default tempo defined in the Settings class
	 * @throws Exception If an error occurs in generating this MidiFile.
	 */
	public MidiFile() throws Exception{
		this("output.mid", Settings.TEMPO_DEFAULT);
	}
	
	/**
	 * This constructs a MidiFile with the given filename and tempo.
	 * @param filename The namme of this file as a String.
	 * @param tempo The tempo to create this file with.
	 * @throws Exception If an error occurs in generating this MidiFile.
	 */
	public MidiFile(String filename, int tempo) throws Exception{
		// Set fields
		this.tempo = tempo;
		if(filename == null)
			this.filename = "output.mid";
		else
			this.filename = filename;
		// Setup the file for writing
		setup();
	}
	
	/**
	 * This plays a note defined by the given Note object.
	 * @param n The Note object defining what to play.
	 */
	public void playNote(Note n){
		// Turn on the note with the given properties and turn it off after the given note's duration
		turnOnNote(n.getNote(), n.getVelocity(), n.getStartingTime());
		turnOffNote(n.getNote(), n.getVelocity(), n.getStartingTime() + n.getDuration());
	}
	
	/**
	 * This will turn on a note with the given properties.
	 * @param note The number representing the note to be played.
	 * @param velocity The velocity of this note.
	 * @param timestamp What tick/timestamp to play this note at.
	 */
	public void turnOnNote(int note, int velocity, int timestamp){
		// Create a message
		ShortMessage mm = new ShortMessage();
		// Try to set the message
		try {
			mm.setMessage(0x90, note, velocity);
		} catch (InvalidMidiDataException e) {
			Settings.fail("UNABLE TO WRITE NOTE: (" + note + ") AT TICK: (" + timestamp + ")");
			e.printStackTrace();
		}
		// Create an event and add it to the track
		MidiEvent me = new MidiEvent(mm,(long)timestamp);
		t0.add(me);
	}
	
	/**
	 * This will turn off a note at the given time with the given velocity.
	 * @param note The note to turn off.
	 * @param velocity The velocity of the note at turn off time.
	 * @param timestamp The tick/timestamp to turn off the note.
	 */
	public void turnOffNote(int note, int velocity, int timestamp){
		// Create a message
		ShortMessage mm = new ShortMessage();
		// Try to set the message
		try {
			mm.setMessage(0x80, note, velocity);
		} catch (InvalidMidiDataException e) {
			Settings.fail("UNABLE TO WRITE NOTE: (" + note + ") AT TICK: (" + timestamp + ")");
			e.printStackTrace();
		}
		// Create an event and add it to the track
		MidiEvent me = new MidiEvent(mm,(long)timestamp);
		t0.add(me);
	}
	
	/**
	 * This will write the sequence of this MidiFile to a file with the set filename.
	 * @throws Exception If there is an error writing the file to disk.
	 */
	public void finish() throws Exception{
		File f = new File(filename);
		try {
			MidiSystem.write(s,1,f);
		} catch (IOException e) {
			Settings.failAndHalt("UNABLE TO WRITE TO MIDI FILE");
			e.printStackTrace();
		}
	}
	
	/**
	 * This is a helper method to setup the MidiFile object for writing notes
	 * @throws Exception If an error occurs in generating this MidiFile.
	 */
	private void setup() throws Exception{
		try {
			// Create a new MIDI sequence with 24 ticks per beat
			this.s = new Sequence(javax.sound.midi.Sequence.PPQ, TICKS_PER_BEAT);

			// Obtain a MIDI track from the sequence
			t0 = s.createTrack();
	
			// General MIDI sysex -- turn on General MIDI sound set
			byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
			SysexMessage sm = new SysexMessage();
			sm.setMessage(b, 6);
			MidiEvent me = new MidiEvent(sm,(long)0);
			t0.add(me);
	
			// Set tempo (meta event)
			MetaMessage mt = new MetaMessage();
			int micros = 60000 / (TICKS_PER_BEAT * tempo);
			byte byte0 = (byte) (micros & 0x000000FF);
			byte byte1 = (byte) (micros>>8 & 0x000000FF);
			byte byte2 = (byte) (micros>>16 & 0x000000FF);
			byte[] bt = {byte0, byte1, byte2};
			mt.setMessage(0x51 ,bt, 3);
			me = new MidiEvent(mt,(long)0);
			t0.add(me);
	
			// Set track name (meta event)
			mt = new MetaMessage();
			String TrackName = new String("midifile track");
			mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
			me = new MidiEvent(mt,(long)0);
			t0.add(me);
	
			// Set omni on
			ShortMessage mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7D,0x00);
			me = new MidiEvent(mm,(long)0);
			t0.add(me);
	
			// Set poly on
			mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7F,0x00);
			me = new MidiEvent(mm,(long)0);
			t0.add(me);
	
			// Set instrument to Piano
			mm = new ShortMessage();
			mm.setMessage(0xC0, 0x00, 0x00);
			me = new MidiEvent(mm,(long)0);
			t0.add(me);
		} catch (Exception e){
			Settings.failAndHalt("UNABLE TO CREATE MIDIFILE!");
			e.printStackTrace();
		}
	}
	
}