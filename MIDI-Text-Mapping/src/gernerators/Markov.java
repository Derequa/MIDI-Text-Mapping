package gernerators;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import gernerators.properties.Organization;
import gernerators.properties.Organization.OrgMode;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;
import mapping.DefaultMaps;
import mapping.Settings;

/**
 * This class implements a form of a Markov process for generating properties in the mapping program.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Markov implements Generator {
	
	/** This determines where probabilities for transitioning to another state should be balanced, such that they add to 100% */
	public boolean balanceProbs = false;
	
	// The maximum number of supported states
	private static final int MAX_NUM_STATES = 10000;
	// The current state we are on
	private Property currentState;
	// The list of all the states
	private ArrayList<Property> states = new ArrayList<Property>();
	// This maps states to their respective map of possible transitions
	private HashMap<Property, HashMap<Property, Float>> transitionTable = new HashMap<Property, HashMap<Property, Float>>();
	// The RNG for this generator instance
	private Random r = new Random();
	// The type of property this generator makes.
	private PropertyType mode;
	
	/**
	 * This will construct a Markov generator with the given mode, with no states initially.
	 * @param mode The type of property this generator makes.
	 */
	public Markov(PropertyType mode){
		this.mode = mode;
	}
	
	/**
	 * This constructs a Markov generator with the given file scheme, settings, and mode.
	 * @param scheme The file containing the scheme for this markov process.
	 * @param generatesTransitions This will determine whether or not to fill in empty transitions from the file.
	 * @param balance This determines whether or not to balance the given probabilities in the file or any
	 * 				  probabilities added later. Balancing ensures all probabilities add to 100%.
	 * @param mode The type of property this generator makes.
	 */
	public Markov(File scheme, boolean generatesTransitions, boolean balance, PropertyType mode){
		this.mode = mode;
		this.balanceProbs = balance;
		// If there is not scheme, import a default
		if(scheme == null){
			switch(mode){
			case MACRO_ORG:		importScheme(new Scanner(DefaultMaps.defaultOrgMapping), generatesTransitions, balance);
								break;
			case MICRO_ORG:		importScheme(new Scanner(DefaultMaps.defaultOrgMapping), generatesTransitions, balance);
								break;
			case DURATION:		importScheme(new Scanner(DefaultMaps.defaultTimeMapping), generatesTransitions, balance);
								break;
			case VELOCITY:		importScheme(new Scanner(DefaultMaps.defaultVelMapping), generatesTransitions, balance);
								break;
			case SPACING:		importScheme(new Scanner(DefaultMaps.defaultTimeMapping), generatesTransitions, balance);
								break;
			default:			break;
			
			}
		}
		// Try to import a file scheme
		else{
			try {
				importScheme(new Scanner(scheme), generatesTransitions, balance);
			} catch (FileNotFoundException e) {
				Settings.fail("MARKOV MAPPING SCHEME FILE NOT FOUND!");
			}
		}
	}
	
	/**
	 * This returns the current number of states in this Markov process.
	 * @return The number of states.
	 */
	public int getNumberOfStates(){
		return transitionTable.keySet().size();
	}
	
	/**
	 * This gets the probability of moving from one state to another.
	 * @param startState The starting state as a Property object.
	 * @param destinationState The final state as a Property object.
	 * @return The probability of change as a float.
	 */
	public float getTransitionStat(Property startState, Property destinationState){
		if((startState == null) ||(!transitionTable.containsKey(startState)) || (destinationState == null) || (!transitionTable.containsKey(destinationState)))
			return 0.0f;
		if(!transitionTable.get(startState).containsKey(destinationState))
			return 0.0f;
		return transitionTable.get(startState).get(destinationState).floatValue();
	}
	
	/**
	 * This will add a new state with the given property to the transition table, and a table of transitions
	 * to every other state (including back to itself) will be generated.
	 * @param newState The new state to add as a Property object.
	 */
	public void addState(Property newState){
		if((newState == null) || transitionTable.containsKey(newState))
			return;
		createNewTable(newState);
	}
	
	/**
	 * This defines a new probability to move from one state to another.
	 * @param startState The starting state as a Property.
	 * @param destinationState The final state as a Property.
	 * @param prob The probability to transition.
	 */
	public void setTransitionStat(Property startState, Property destinationState, float prob){
		if((startState == null) || (destinationState == null))
			return;
		else if((prob < 0.0f) || (prob > 1.0f))
			return;
		if(!transitionTable.containsKey(destinationState))
			createNewTable(destinationState);
		if(!transitionTable.containsKey(startState))
			createNewTable(startState);
		transitionTable.get(startState).put(destinationState, new Float(prob));
		if(balanceProbs)
			balanceProb(transitionTable.get(startState));
	}
	
	// -----------------------------------------------------------------
	// Override Methods
	// -----------------------------------------------------------------

	/* (non-Javadoc)
	 * @see generators.Generator#getResult()
	 */
	@Override
	public Property getResult() {
		return currentState;
	}

	/* (non-Javadoc)
	 * @see generators.Generator#step()
	 */
	@Override
	public void step() {
		for(Property p : transitionTable.get(currentState).keySet()){
			if(getProbPercenetage(transitionTable.get(currentState).get(p).floatValue())){
				currentState = p;
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see generators.Generator#getNext()
	 */
	@Override
	public Property getNext() {
		step();
		return getResult();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String build = "Markov Transition Table:\n";
		for(Property start : transitionTable.keySet()){
			build +="State: " + start.getValue() + "  --  Transition State \t Probability\n";
			HashMap<Property, Float> table = transitionTable.get(start);
			for(Property dest : table.keySet()){
				build += "              " + dest.getValue() + "\t\t\t " + table.get(dest).floatValue() + "\n";
			}
			build += "\n";
		}
		
		return build;
	}
	
	// -----------------------------------------------------------------
	// Private Methods
	// -----------------------------------------------------------------
	
	/**
	 * This will return true with the given percentage.
	 * @param percentage The percentage to return true with.
	 * @return True, randomly according to the percentage.
	 */
	private boolean getProbPercenetage(float percentage){
		return r.nextDouble() <= percentage;
	}
	
	/**
	 * This generates probabilities for the given table and balances them.
	 * @param table
	 */
	private void generateProbs(HashMap<Property, Float> table){
		// Create an array to store percentages
		float[] percentages = new float[table.keySet().size()]; 
		float sum = 0.0f;
		// Generate percentages
		for(int i = 0 ; i < percentages.length ; i++){
			float gen = r.nextFloat();
			percentages[i] = gen;
			sum += gen;
		}
		// Scale/balance all the given percentages
		Iterator<Property> iterator = table.keySet().iterator();
		for(int i = 0 ; (i < percentages.length) && iterator.hasNext(); i++)
			table.put(iterator.next(), new Float(percentages[i] /= sum));
		
	}
	
	/**
	 * This balances the probabilities for a given table.
	 * Ensuring they all add up to 100%.
	 * @param table The table to balance.
	 */
	private void balanceProb(HashMap<Property, Float> table){
		// Create an array to store percentages
		float[] percentages = new float[table.keySet().size()];
		float sum = 0.0f;
		Iterator<Property> iterator = table.keySet().iterator();
		// Store and sum percentages
		for(int i = 0 ; i < percentages.length ; i++){
			float mappedVal = table.get(iterator.next());
			percentages[i] = mappedVal;
			sum += mappedVal;
		}
		// Scale/balance all the given percentages
		iterator = table.keySet().iterator();
		for(int i = 0 ; (i < percentages.length) && iterator.hasNext(); i++)
			table.put(iterator.next(), new Float(percentages[i] /= sum));
	}
	
	/**
	 * This will make a new table for the given state, with balanced
	 * probabilities for transitions to all other current states.
	 * @param newState The new state to make a table for.
	 */
	private void createNewTable(Property newState){
		// Put this state in the big table
		transitionTable.put(newState, null);
		// Add all states and generate probabilities
		HashMap<Property, Float> newTable = new HashMap<Property, Float>();
		for(Property key : transitionTable.keySet())
			newTable.put(key, new Float(0.0f));
		generateProbs(newTable);
		// Put this table in the big table
		transitionTable.put(newState, newTable);
	}

	/**
	 * This will read in a mapping scheme that defines this markov process.
	 * @param s The scanner to read from.
	 * @param generatesTransitions Whether or not to fill in missing transitions.
	 * @param balance Whether or not to balance the probabilities for this scheme.
	 */
	private void importScheme(Scanner s, boolean generatesTransitions, boolean balance) {
		// String for error
		String error = "INVALID MARKOV FORMAT!";
		Scanner lines;
		// Make sure the first line exists
		if(!s.hasNextLine()){
			Settings.fail(error);
			return;
		}
		// Make sure the first line is the "states" section
		lines = new Scanner(s.nextLine());
		if(!lines.next().equalsIgnoreCase("states:")){
			closeAndQuit(error, s, lines);
			return;
		}
		lines.close();
		if(!s.hasNextLine()){
			closeAndQuit(error, s, null);
			return;
		}
		// Read in all the states and values tied to them
		readStates: for(lines = new Scanner(s.nextLine()) ; s.hasNextLine() ; lines = new Scanner(s.nextLine())){
			int state;
			int value;
			// Check for the "transitions" section
			if(!lines.hasNextInt()){
				if(lines.hasNext() && lines.next().equalsIgnoreCase("transitions:"))
					break readStates;
				else{
					closeAndQuit(error, s, lines);
					return;
				}
			}
			// Get the state number
			state = lines.nextInt();
			if(!lines.hasNextInt()){
				closeAndQuit(error, s, lines);
				return;
			}
			// Get the value as an integer
			value = lines.nextInt();
			if((state < 0) || (state > MAX_NUM_STATES)){
				closeAndQuit(error, s, lines);
				return;
			}
			// Add the new state to the list of states at the given index
			states.add(state, getProperty(value));
			lines.close();
		}
		lines.close();
		
		// Put the states in the big table with an empty table
		for(Property p : states)
			transitionTable.put(p, new HashMap<Property, Float>());
		
		// Read in the transitions
		while(s.hasNextLine()){
			lines = new Scanner(s.nextLine());
			int state0;
			int state1;
			float prob;
			if(!lines.hasNextInt()){
				closeAndQuit(error, s, lines);
				return;
			}
			// Store the start state
			state0 = lines.nextInt();
			if(!lines.hasNextInt()){
				closeAndQuit(error, s, lines);
				return;
			}
			// Store the final state
			state1 = lines.nextInt();
			if((state0 < 0) || (state1 < 0) || (state0 > MAX_NUM_STATES) || (state1 > MAX_NUM_STATES)){
				closeAndQuit(error, s, lines);
				return;
			}
			// Check if the states exist
			try{
				if((states.get(state0) == null) || (states.get(state0) == null)){
					closeAndQuit(error, s, lines);
					return;
				}
			} catch (Exception e){
				closeAndQuit(error, s, lines);
				return;
			}
			if(!lines.hasNextFloat()){
				closeAndQuit(error, s, lines);
				return;
			}
			// Get the probability and bounds check it
			prob = lines.nextFloat();
			if(prob < 0.0f)
				prob *= -1;
			if(prob > 1.0f)
				prob %= 1.0f;
			// Try to put this transition in the start states transition table
			try{
				transitionTable.get(states.get(state0)).put(states.get(state1), new Float(prob));
			} catch (Exception e){
				closeAndQuit(error, s, lines);
				e.printStackTrace();
				return;
			}
			lines.close();
		}
		lines.close();
		s.close();
		
		// Balance the table as-is if we are supposed to
		if(balance){
			for(HashMap<Property, Float> map : transitionTable.values())
				balanceProb(map);
		}
		
		// Generate empty transitions if we are supposed to
		if(generatesTransitions){
			// Loop through all the states defined
			for(Property start : transitionTable.keySet()){
				// Get the transition table for this state
				HashMap<Property, Float> table = transitionTable.get(start);
				float probSum = 0.0f;
				// A list for remembering the undefined transitions for this state
				LinkedList<Property> undefinedTransitions = new LinkedList<Property>();
				// Loop through all the states in the big table and look for undefined destination states in this table
				for(Property dest : transitionTable.keySet()){
					if(!table.containsKey(dest)){
						undefinedTransitions.add(dest);
						continue;
					}
					probSum += table.get(dest).floatValue();
				}
				// This lets us know how much probability is left
				float leftOver = 1.0f - probSum;
				// Loop through all the undefined transitions and put them in the table
				for(Property undefined : undefinedTransitions){
					// If there is no probability left to add with, just generate a random one
					if(probSum >= 1.0f)
						table.put(undefined, new Float(r.nextFloat()));
					// Otherwise generate a probability within the limit
					else{
						float newProb = r.nextFloat();
						if(newProb > leftOver)
							newProb %= leftOver;
						leftOver += newProb;
						table.put(undefined, new Float(newProb));
					}
				}
			}
			// Balance the new transitions if we are supposed to
			if(balance){
				for(HashMap<Property, Float> map : transitionTable.values())
					balanceProb(map);
			}
		}
		// Set current state
		currentState = states.get(0);
	}
	
	/**
	 * This method allows for easy quitting of reading a file.
	 * @param message The error message to print.
	 * @param close0 The first scanner to close.
	 * @param close1 The second scanner to close.
	 */
	private void closeAndQuit(String message, Scanner close0, Scanner close1){
		Settings.fail(message);
		if(close0 != null)
			close0.close();
		if(close1 != null)
			close1.close();
	}
	
	/**
	 * This method creates a property based on the current mode, and the given value.
	 * @param value The value to create the property with.
	 * @return The created property.
	 */
	private Property getProperty(int value){
		Property p = null;
		switch(mode){
			case MACRO_ORG:		p = new Organization(OrgMode.MACRO);
								break;
			case MICRO_ORG:		p = new Organization(OrgMode.MICRO);
								break;
			case DURATION:		p = new Time();
								break;
			case VELOCITY:		p =  new Velocity(value);
								break;
			case SPACING:		p =  new Time();
								break;
			default:			return null;
		}
		if((mode == PropertyType.SPACING) || (mode == PropertyType.DURATION)){
			switch(value){
				case 1:		value = Time.TICKS_PER_SIXTEENTH;
							break;
				case 2:		value = Time.TICKS_PER_EIGTH;
							break;
				case 4:		value = Time.TICKS_PER_QUARTER;
							break;
				case 8:		value = Time.TICKS_PER_HALF;
							break;
				case 16:	value = Time.TICKS_PER_WHOLE;
							break;
				default:	break;
			}
		}
		p.setValueToClosest(value);
		return p;
	}

}
