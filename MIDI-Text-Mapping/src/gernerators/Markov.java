package gernerators;

import java.util.HashMap;
import java.util.LinkedList;

public class Markov implements Generator {
	
	public boolean balanceProbs = false;
	private int numStates;
	private LinkedList<HashMap<Integer, Float>> transitionTable;
	
	public Markov(int numStates){
		this.numStates = numStates - 1;
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
		int count = 0;
		for(HashMap<Integer, Float> table : transitionTable){
			generateProbFor(count++, numStates, table);
			balanceProb(table);
		}
		HashMap<Integer, Float> newTable = new HashMap<Integer, Float>();
		generateProbs(numStates, newTable);
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNext() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void generateProbFor(int self, int other, HashMap<Integer, Float> table){
		
	}
	
	private void generateProbs(int self, HashMap<Integer, Float> table){
		
	}
	
	private void balanceProb(HashMap<Integer, Float> table){
		
	}

}
