package gernerators;

import java.util.Random;

public class FNoise implements Generator {
	
	int counter = 0;
	int[] dice;
	Random roller = new Random();

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
		int result = 0;
		for(int i = 0 ; i < dice.length ; i++)
			result += dice[i];
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
	}

	@Override
	public int getNext() {
		int result = getResult();
		step();
		return result;
	}
}
