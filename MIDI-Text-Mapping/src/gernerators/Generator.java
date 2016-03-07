package gernerators;

import gernerators.properties.Property;

public interface Generator {
	
	public enum GeneratorType {
		NONE,
		FNOISE,
		KARPLUS,
		MARKOV,
		TRIANGULAR
	}
	
	public Property getResult();
	public void step();
	public Property getNext();
}
