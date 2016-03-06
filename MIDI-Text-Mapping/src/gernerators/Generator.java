package gernerators;

import gernerators.properties.Property;

public interface Generator {
	public Property getResult();
	public void step();
	public Property getNext();
}
