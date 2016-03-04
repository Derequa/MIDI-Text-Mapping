package music;

import gernerators.FNoise;
import gernerators.KarplusStrong;
import gernerators.Markov;

public class Tester {
	public static void main(String[] args){
		testFNoise();
	}
	
	public static void testMarkov(){
		System.out.println("Creating Markov Object...");
		Markov m = new Markov(4);
		System.out.println(m);
		System.out.println("\nCurrent State: " + m.getResult());
		for(int i = 0 ; i < 3 ; i++){
			System.out.println("Transitioning...");
			m.step();
			System.out.println("\nCurrent State: " + m.getResult());
		}
	}
	
	public static void testKarplus(){
		System.out.println("Creating Karplus Object...");
		KarplusStrong k = new KarplusStrong(10, 20, 10);
		System.out.println(k);
		while(!k.isPastThreshold(10)){
			k.step();
			System.out.println(k);
		}
		k.step();
		System.out.println(k);
	}
	
	public static void testFNoise(){
		String build = "";
		System.out.println("Creating FNoise Object...");
		FNoise f = new FNoise(3);
		for(int c = 0 ; c < 3 ; c++){
			build += "[ ";
			for(int i = 0 ; i < 8 ; i++){
				build += f.getNext() + ", ";
			}
			build += " ]\n";
		}
		System.out.println(build);
	}
}
