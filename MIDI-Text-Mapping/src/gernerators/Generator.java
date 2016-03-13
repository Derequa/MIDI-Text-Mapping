package gernerators;

import gernerators.properties.Property;

/**
 * This interface defines the methods that a Generator class must implement.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public interface Generator {
	
	/**
	 * This defines the different types of implemented generators.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	public enum GeneratorType {
		NONE,
		FNOISE,
		KARPLUS,
		MARKOV,
		TRIANGULAR,
		CONSTANT
	}
	
	/**
	 * This gets the current Property object that has been generated.
	 * It does not step the generator.
	 * @return The current generated property.
	 */
	public Property getResult();
	
	/**
	 * This steps the generator, setting a new current result.
	 */
	public void step();
	
	/**
	 * This steps the generator and returns the current result set.
	 * It has the same result as calling step() and getResult() in succession.
	 * @return The next result.
	 */
	public Property getNext();
}
