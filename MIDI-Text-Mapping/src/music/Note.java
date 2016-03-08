package music;

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
	
	public Note(int midiNote, int velocity, int start, int duration){
		this.midiNote = midiNote;
		this.velocity = velocity;
		this.start = start;
		this.duration = duration;
	}
	
	public int getNote(){
		return midiNote;
	}
	
	public int getVelocity(){
		return velocity;
	}
	
	public int getStartingTime(){
		return start;
	}
	
	public int getDuration(){
		return duration;
	}
	
	
	public void setNote(int newNote){
		midiNote = newNote;
	}
	
	public void setVelocity(int newVelocity){
		velocity = newVelocity;
	}
	
	public void setStart(int newStart){
		start = newStart;
	}
	
	public void setDuration(int newDuration){
		duration = newDuration;
	}

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

	@Override
	public int compareTo(Note arg0) {
		if(equals(arg0))
			return 0;
		else if(start > arg0.getStartingTime())
			return 1;
		else
			return -1;
	}
	
	public String toString(){
		return "[ Note: " + midiNote + " Velocity: " + velocity + " Start: " + start + " Duration " + duration + " ]\n";
	}
}
