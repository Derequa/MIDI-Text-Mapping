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
 */
public class MidiFile {
	
	public static final int TICKS_PER_BEAT = 24;
	
	private Track t0;
	private Sequence s;
	private String filename;
	
	public MidiFile(){
		this("output.mid");
	}
	
	public MidiFile(String filename){
		if(filename == null)
			this.filename = "output.mid";
		else
			this.filename = filename;
		setup();
	}
	
	private void setup(){
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
	
			// set tempo (meta event)
			MetaMessage mt = new MetaMessage();
	        byte[] bt = {0x02, (byte)0x00, 0x00};
			mt.setMessage(0x51 ,bt, 3);
			me = new MidiEvent(mt,(long)0);
			t0.add(me);
	
			// set track name (meta event)
			mt = new MetaMessage();
			String TrackName = new String("midifile track");
			mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
			me = new MidiEvent(mt,(long)0);
			t0.add(me);
	
			// set omni on
			ShortMessage mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7D,0x00);
			me = new MidiEvent(mm,(long)0);
			t0.add(me);
	
			// set poly on
			mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7F,0x00);
			me = new MidiEvent(mm,(long)0);
			t0.add(me);
	
			// set instrument to Piano
			mm = new ShortMessage();
			mm.setMessage(0xC0, 0x00, 0x00);
			me = new MidiEvent(mm,(long)0);
			t0.add(me);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void playNote(Note n){
		turnOnNote(n.getNote(), n.getVelocity(), n.getStartingTime());
		turnOffNote(n.getNote(), n.getVelocity(), n.getStartingTime() + n.getDuration());
	}
	
	public void turnOnNote(int note, int velocity, int timestamp){
		ShortMessage mm = new ShortMessage();
		try {
			mm.setMessage(0x90, note, velocity);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		MidiEvent me = new MidiEvent(mm,(long)timestamp);
		t0.add(me);
	}
	
	public void turnOffNote(int note, int velocity, int timestamp){
		ShortMessage mm = new ShortMessage();
		try {
			mm.setMessage(0x80, note, velocity);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			System.exit(1);
		}
		MidiEvent me = new MidiEvent(mm,(long)timestamp);
		t0.add(me);
	}
	
	public void finish(){
		File f = new File(filename);
		try {
			MidiSystem.write(s,1,f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
} //midifile