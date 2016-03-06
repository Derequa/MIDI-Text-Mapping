package gernerators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import gernerators.properties.Property;

public class Markov implements Generator {
	
	public boolean balanceProbs = false;
	private Property currentState;
	private HashMap<Property, HashMap<Property, Float>> transitionTable = new HashMap<Property, HashMap<Property, Float>>();
	private Random r = new Random();
	
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
		for(int i = 0 ; i < transitionTable.size() ; i++){
			build +="State: " + i + "  --  Transition State \t Probability\n";
			HashMap<Property, Float> table = transitionTable.get(i);
			for(Property state : table.keySet())
				build += "              " + state + "\t\t\t " + table.get(state).floatValue() + "\n";
			build += "\n";
		}
		
		return build;
	}
	
	public Property getCurrentState(){
		return currentState;
	}

}
