package gernerators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Markov implements Generator {
	
	public boolean balanceProbs = false;
	private int numStates;
	private int currentState;
	private Random r = new Random();
	private LinkedList<HashMap<Integer, Float>> transitionTable = new LinkedList<HashMap<Integer, Float>>();
	
	public Markov(int numStates){
		this.numStates = numStates - 1;
		currentState = r.nextInt(numStates);
		for(int i = 0 ; i < numStates ; i++){
			HashMap<Integer, Float> table = new HashMap<Integer, Float>();
			for(int j = 0 ; j < numStates ; j++)
				table.put(new Integer(j), new Float(r.nextFloat()));
			balanceProb(table);
			transitionTable.add(table);
		}
	}
	
	public int getNumberOfStates(){
		return numStates + 1;
	}
	
	public float getTransitionStat(int startState, int destinationState){
		if((startState < 0) ||(startState >= numStates) || (destinationState < 0) ||(destinationState >= numStates))
			return 0.0f;
		return transitionTable.get(numStates).get(new Integer(destinationState)).floatValue();
	}
	
	public int addState(){
		numStates++;
		for(HashMap<Integer, Float> table : transitionTable){
			generateProbFor(numStates, table);
			balanceProb(table);
		}
		HashMap<Integer, Float> newTable = new HashMap<Integer, Float>();
		generateProbs(newTable);
		transitionTable.addLast(newTable);
		return numStates + 1;
	}
	
	public void addTransitionStat(int startState, int destinationState, float prob){
		if((startState < 0) ||(startState >= numStates) || (destinationState < 0) ||(destinationState >= numStates))
			return;
		else if((prob < 0.0f) || (prob > 1.0f))
			return;
		transitionTable.get(startState).put(new Integer(destinationState), new Float(prob));
		if(balanceProbs)
			balanceProb(transitionTable.get(startState));
	}	

	@Override
	public int getResult() {
		return currentState;
	}

	@Override
	public void step() {
		for(Integer i : transitionTable.get(currentState).keySet()){
			if(getProbPercenetage(transitionTable.get(currentState).get(i).floatValue())){
				currentState = i.intValue();
				return;
			}
		}
	}

	@Override
	public int getNext() {
		step();
		return getResult();
	}
	
	private boolean getProbPercenetage(float percentage){
		return r.nextDouble() <= percentage;
	}
	
	private void generateProbFor(int other, HashMap<Integer, Float> table){
		float gen = r.nextFloat();
		table.put(new Integer(other), new Float(gen));
		balanceProb(table);
	}
	
	private void generateProbs(HashMap<Integer, Float> table){
		float[] percentages = new float[table.keySet().size()];
		float sum = 0.0f;
		for(int i = 0 ; i < percentages.length ; i++){
			float gen = r.nextFloat();
			percentages[i] = gen;
			sum += gen;
		}
		Iterator<Integer> iterator = table.keySet().iterator();
		for(int i = 0 ; (i < percentages.length) && iterator.hasNext(); i++)
			table.put(iterator.next(), new Float(percentages[i] /= sum));
		
	}
	
	private void balanceProb(HashMap<Integer, Float> table){
		float[] percentages = new float[table.keySet().size()];
		float sum = 0.0f;
		Iterator<Integer> iterator = table.keySet().iterator();
		for(int i = 0 ; i < percentages.length ; i++){
			float mappedVal = table.get(iterator.next());
			percentages[i] = mappedVal;
			sum += mappedVal;
		}
		iterator = table.keySet().iterator();
		for(int i = 0 ; (i < percentages.length) && iterator.hasNext(); i++)
			table.put(iterator.next(), new Float(percentages[i] /= sum));
	}
	
	public String toString(){
		String build = "Markov Transition Table:\n";
		for(int i = 0 ; i < transitionTable.size() ; i++){
			build +="State: " + i + "  --  Transition State \t Probability\n";
			HashMap<Integer, Float> table = transitionTable.get(i);
			for(Integer state : table.keySet())
				build += "              " + state.intValue() + "\t\t\t " + table.get(state).floatValue() + "\n";
			build += "\n";
		}
		
		return build;
	}
	
	public int getCurrentState(){
		return currentState;
	}

}
