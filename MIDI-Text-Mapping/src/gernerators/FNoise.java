package gernerators;

import java.util.Random;

public class FNoise implements Generator {
	
	private int result = -1;
	private int counter = 0;
	private int[] dice;
	private Random roller = new Random();

	public FNoise(int numDice){
		if(numDice > 32)
			numDice = 32;
		else if(numDice < 1)
			numDice = 1;
		dice = new int[numDice];
		for(int i = 0 ; i < dice.length ; i++)
			dice[i] = roller.nextInt(6) + 1;
	}
	
	public int getResult(){
		return result;
	}

	public void step(){
		int oldCounter = counter;
		counter++;
		int mask = 1;
		for(int i = 0 ; i < dice.length ; i++){
			int oldMasked = oldCounter & mask;
			int currentMasked = counter & mask;
			if(oldMasked != currentMasked)
				dice[i] = roller.nextInt(6) + 1;
			mask <<= 1;
		}
		result = 0;
		for(int i = 0 ; i < dice.length ; i++)
			result += dice[i];
	}

	@Override
	public int getNext() {
		step();
		return getResult();
	}
	
	public int getNumDice(){
		return dice.length;
	}
}
