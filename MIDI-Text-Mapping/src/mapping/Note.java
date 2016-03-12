package mapping;

/**
 * This class wraps and abstracts a MIDI note for
 * processing before writing to file.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Note implements Comparable<Note>{
	
	/** The integer value representing the note */
	private int midiNote;
	/** The velocity the note is to be played at */
	private int velocity;
	/** The starting time-stamp of the note */
	private int start;
	/** How long the note should last */
	private int duration;
	
	/**
	 * This constructs a Note object according to the given parameters.
	 * @param midiNote The integer value representing the note.
	 * @param velocity The velocity the note is to be played at.
	 * @param start The starting time of the note.
	 * @param duration The duration (in ticks) of the note.
	 */
	public Note(int midiNote, int velocity, int start, int duration){
		this.midiNote = midiNote;
		this.velocity = velocity;
		this.start = start;
		this.duration = duration;
	}
	
	// -----------------------------------------------------------------
	// Getter Methods
	// -----------------------------------------------------------------
	
	/**
	 * This gets the integer value of the note.
	 * @return The value of the note as an integer.
	 */
	public int getNote(){
		return midiNote;
	}
	
	/**
	 * This gets the integer value of the velocity.
	 * @return The value of the velocity as an integer.
	 */
	public int getVelocity(){
		return velocity;
	}
	
	/**
	 * This gets the starting time of the note (in ticks).
	 * @return The starting time of the note as an integer.
	 */
	public int getStartingTime(){
		return start;
	}
	
	/**
	 * This gets the duration of the note (in ticks).
	 * @return The duration of the note as an integer.
	 */
	public int getDuration(){
		return duration;
	}
	
	// -----------------------------------------------------------------
	// Setter Methods
	// -----------------------------------------------------------------
	
	/**
	 * This sets the note value to the given value.
	 * @param newNote The value to set the note to.
	 */
	public void setNote(int newNote){
		if(newNote < 0)
			newNote *= -1;
		if(newNote > 127)
			newNote %= 127;
		midiNote = newNote;
	}
	
	/**
	 * This sets the note velocity value to the given value.
	 * @param newNote The value to set the note velocity to.
	 */
	public void setVelocity(int newVelocity){
		if(newVelocity < 0)
			newVelocity *= -1;
		if(newVelocity > 127)
			newVelocity %= 127;
		velocity = newVelocity;
	}
	
	/**
	 * This sets the starting time value (in ticks) to the given value.
	 * @param newNote The value to set the starting time to.
	 */
	public void setStart(int newStart){
		if(newStart < 0)
			newStart  = 0;
		start = newStart;
	}
	
	/**
	 * This sets the duration value (in ticks) to the given value.
	 * @param newNote The value to set duration to.
	 */
	public void setDuration(int newDuration){
		if(newDuration < 0)
			newDuration  = 0;
		duration = newDuration;
	}
	
	// -----------------------------------------------------------------
	// Generic Overriden Methods
	// -----------------------------------------------------------------

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + duration;
		result = prime * result + midiNote;
		result = prime * result + start;
		result = prime * result + velocity;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Note)) {
			return false;
		}
		Note other = (Note) obj;
		if (duration != other.duration) {
			return false;
		}
		if (midiNote != other.midiNote) {
			return false;
		}
		if (start != other.start) {
			return false;
		}
		if (velocity != other.velocity) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(mapping.Note)
	 */
	@Override
	public int compareTo(Note arg0) {
		if(equals(arg0))
			return 0;
		else if(start > arg0.getStartingTime())
			return 1;
		else
			return -1;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "[ Note: " + midiNote + " Velocity: " + velocity + " Start: " + start + " Duration " + duration + " ]\n";
	}
}
