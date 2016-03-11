package gernerators;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.Line;

import gernerators.properties.Organization;
import gernerators.properties.Organization.OrgMode;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;
import mapping.Settings;

public class Markov implements Generator {
	
	private static final int MAX_NUM_STATES = 10000;
	public boolean balanceProbs = false;
	private Property currentState;
	private ArrayList<Property> states = new ArrayList<Property>();
	private HashMap<Property, HashMap<Property, Float>> transitionTable = new HashMap<Property, HashMap<Property, Float>>();
	private Random r = new Random();
	PropertyType mode;
	
	private String defaultMicroMapping = "states:\n"
								  + "0 0\n"
								  + "1 4\n"
								  + "2 7\n"
								  + "3 -4\n"
								  + "4 -7\n"
								  + "transitions:\n"
								  + "0 0 .2\n"
								  + "0 1 .3\n"
								  + "0 2 .1\n"
								  + "0 3 .2\n"
								  + "0 4 .2\n"
								  + "1 0 .4\n"
								  + "1 1 .1\n"
								  + "1 2 .1\n"
								  + "1 3 .2\n"
								  + "1 4 .2\n"
								  + "2 0 .5\n"
								  + "2 1 .1\n"
								  + "2 2 .1\n"
								  + "2 3 .1\n"
								  + "2 4 .2\n"
								  + "3 0 .2\n"
								  + "3 1 .2\n"
								  + "3 2 .2\n"
								  + "3 3 .2\n"
								  + "3 4 .2\n"
								  + "4 0 .1\n"
								  + "4 1 .6\n"
								  + "4 2 .1\n"
								  + "4 3 .1\n"
								  + "4 4 .1\n";
	
	public Markov(PropertyType mode){
		this.mode = mode;
	}
	
	public Markov(File scheme, boolean generatesTransitions, boolean balance, PropertyType mode){
		this.mode = mode;
		if(scheme == null){
			switch(mode){
			case MACRO_ORG:
				break;
			case MICRO_ORG:		importScheme(new Scanner(defaultMicroMapping), generatesTransitions, balance);
				break;
			case DURATION:
				break;
			case VELOCITY:
				break;
			case SPACING:
				break;
			default:
				break;
			
			}
		}
		else{
			try {
				importScheme(new Scanner(scheme), generatesTransitions, balance);
			} catch (FileNotFoundException e) {
				Settings.fail("MARKOV MAPPING SCHEME FILE NOT FOUND");
			}
		}
		Settings.debugMessage(this.toString());
	}
	
	public int getNumberOfStates(){
		return transitionTable.keySet().size();
	}
	
	public float getTransitionStat(Property startState, Property destinationState){
		if((startState == null) ||(!transitionTable.containsKey(startState)) || (destinationState == null) || (!transitionTable.containsKey(destinationState)))
			return 0.0f;
		return transitionTable.get(startState).get(destinationState).floatValue();
	}
	
	public void addState(Property newState){
		if((newState == null) || transitionTable.containsKey(newState))
			return;
		createNewTable(newState);
		
	}
	
	public void setTransitionStat(Property startState, Property destinationState, float prob){
		if((startState == null) || (destinationState == null))
			return;
		else if((prob < 0.0f) || (prob > 1.0f))
			return;
		if(!transitionTable.containsKey(destinationState)){
			createNewTable(destinationState);
		}
		if(!transitionTable.containsKey(startState)){
			createNewTable(startState);
		}
		transitionTable.get(startState).put(destinationState, new Float(prob));
		if(balanceProbs)
			balanceProb(transitionTable.get(startState));
	}	

	@Override
	public Property getResult() {
		return currentState;
	}

	@Override
	public void step() {
		for(Property p : transitionTable.get(currentState).keySet()){
			if(getProbPercenetage(transitionTable.get(currentState).get(p).floatValue())){
				currentState = p;
				return;
			}
		}
	}

	@Override
	public Property getNext() {
		step();
		return getResult();
	}
	
	private boolean getProbPercenetage(float percentage){
		return r.nextDouble() <= percentage;
	}
	
	private void generateProbs(HashMap<Property, Float> table){
		float[] percentages = new float[table.keySet().size()];
		float sum = 0.0f;
		for(int i = 0 ; i < percentages.length ; i++){
			float gen = r.nextFloat();
			percentages[i] = gen;
			sum += gen;
		}
		Iterator<Property> iterator = table.keySet().iterator();
		for(int i = 0 ; (i < percentages.length) && iterator.hasNext(); i++)
			table.put(iterator.next(), new Float(percentages[i] /= sum));
		
	}
	
	private void balanceProb(HashMap<Property, Float> table){
		float[] percentages = new float[table.keySet().size()];
		float sum = 0.0f;
		Iterator<Property> iterator = table.keySet().iterator();
		for(int i = 0 ; i < percentages.length ; i++){
			float mappedVal = table.get(iterator.next());
			percentages[i] = mappedVal;
			sum += mappedVal;
		}
		iterator = table.keySet().iterator();
		for(int i = 0 ; (i < percentages.length) && iterator.hasNext(); i++)
			table.put(iterator.next(), new Float(percentages[i] /= sum));
	}
	
	private void createNewTable(Property newState){
		transitionTable.put(newState, null);
		HashMap<Property, Float> newTable = new HashMap<Property, Float>();
		for(Property key : transitionTable.keySet())
			newTable.put(key, new Float(0.0f));
		generateProbs(newTable);
		transitionTable.put(newState, newTable);
	}
	
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
	
	public Property getCurrentState(){
		return currentState;
	}
	
	private void importScheme(Scanner s, boolean generatesTransitions, boolean balance) {
		String error = "INVALID MARKOV FORMAT!";
		Scanner lines;
		if(!s.hasNextLine()){
			Settings.fail(error);
			return;
		}
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
		readStates: for(lines = new Scanner(s.nextLine()) ; s.hasNextLine() ; lines = new Scanner(s.nextLine())){
			int state;
			int value;
			if(!lines.hasNextInt()){
				if(lines.hasNext() && lines.next().equalsIgnoreCase("transitions:"))
					break readStates;
				
				closeAndQuit(error, s, lines);
				return;
			}
			state = lines.nextInt();
			if(!lines.hasNextInt()){
				closeAndQuit(error, s, lines);
				return;
			}
			value = lines.nextInt();
			if((state < 0) || (state > MAX_NUM_STATES)){
				closeAndQuit(error, s, lines);
				return;
			}
			states.add(state, getProperty(value));
			lines.close();
		}
		lines.close();
		
		for(Property p : states)
			transitionTable.put(p, new HashMap<Property, Float>());
		
		
		while(s.hasNextLine()){
			lines = new Scanner(s.nextLine());
			int state0;
			int state1;
			float prob;
			if(!lines.hasNextInt()){
				closeAndQuit(error, s, lines);
				return;
			}
			state0 = lines.nextInt();
			if(!lines.hasNextInt()){
				closeAndQuit(error, s, lines);
				return;
			}
			state1 = lines.nextInt();
			if((state0 < 0) || (state1 < 0) || (state0 > MAX_NUM_STATES) || (state1 > MAX_NUM_STATES)){
				closeAndQuit(error, s, lines);
				return;
			}
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
			prob = lines.nextFloat();
			if(prob < 0.0f)
				prob *= -1;
			if(prob > 1.0f)
				prob %= 1.0f;
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
		if(balance){
			for(HashMap<Property, Float> map : transitionTable.values())
				balanceProb(map);
		}
		if(generatesTransitions){
			for(Property start : transitionTable.keySet()){
				HashMap<Property, Float> table = transitionTable.get(start);
				float probSum = 0.0f;
				LinkedList<Property> undefinedTransitions = new LinkedList<Property>();
				for(Property dest : transitionTable.keySet()){
					if(!table.containsKey(dest)){
						undefinedTransitions.add(dest);
						continue;
					}
					probSum += table.get(dest).floatValue();
				}
				float leftOver = 1.0f - probSum;
				for(Property undefined : undefinedTransitions){
					if(probSum >= 1.0f)
						table.put(undefined, new Float(r.nextFloat()));
					else{
						float newProb = r.nextFloat();
						if(newProb > leftOver)
							newProb %= leftOver;
						leftOver += newProb;
						table.put(undefined, new Float(newProb));
					}
				}
			}
			if(balance){
				for(HashMap<Property, Float> map : transitionTable.values())
					balanceProb(map);
			}
		}
		currentState = states.get(0);
	}
	
	private void closeAndQuit(String message, Scanner close0, Scanner close1){
		Settings.fail(message);
		if(close0 != null)
			close0.close();
		if(close1 != null)
			close1.close();
	}
	
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
		p.setValueToClosest(value);
		return p;
	}

}
