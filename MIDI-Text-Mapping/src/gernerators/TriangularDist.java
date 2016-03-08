package gernerators;

import gernerators.properties.Organization;
import gernerators.properties.Organization.OrgMode;
import gernerators.properties.Property;
import gernerators.properties.Property.PropertyType;
import gernerators.properties.Time;
import gernerators.properties.Velocity;

public class TriangularDist implements Generator {
	
	public enum DistributionScheme {
		NORMAL,
		LEFT_SLANT,
		RIGHT_SLANT
	}
	
	
	private DistributionScheme mode;
	private Property lastResult;
	private Property worker0;
	private Property worker1;
	
	public TriangularDist(PropertyType typeFlag){
		this(DistributionScheme.NORMAL, typeFlag);
	}
	
	public TriangularDist(DistributionScheme mode, PropertyType typeFlag){
		this.mode = mode;
		switch(typeFlag){
			case DURATION:		lastResult = new Time();
								worker0 = new Time();
								worker1 = new Time();
								break;
			case SPACING:		lastResult = new Time();
								worker0 = new Time();
								worker1 = new Time();
								break;
			case VELOCITY:		lastResult = new Velocity();
								worker0 = new Velocity();
								worker1 = new Velocity();
								break;
			case MICRO_ORG:		lastResult = new Organization(OrgMode.MICRO);
								worker0 = new Organization(OrgMode.MICRO);
								worker1 = new Organization(OrgMode.MICRO);
								break;
			case MACRO_ORG:		lastResult = new Organization(OrgMode.MACRO);
								worker0 = new Organization(OrgMode.MACRO);
								worker1 = new Organization(OrgMode.MACRO);
								break;
			default:			throw new IllegalArgumentException("TYPE ID NOT RECOGNIZED");
		}
		// TODO Add support for more types
		step();
	}
	
	public void changeMode(DistributionScheme newMode){
		if((newMode != DistributionScheme.NORMAL) && 
		   (newMode != DistributionScheme.LEFT_SLANT) && 
		   (newMode != DistributionScheme.RIGHT_SLANT))
			return;
		else
			mode = newMode;
	}

	@Override
	public Property getResult() {
		return lastResult;
	}

	@Override
	public void step() {
		worker0.randomize();
		worker1.randomize();
		switch (mode){
			case NORMAL:		lastResult.setValueToClosest((worker0.getValue() + worker1.getValue()) / 2);
								break;
			case LEFT_SLANT:	if(worker0.getValue() < worker1.getValue())
									lastResult.setValueToClosest(worker0.getValue());
								else
									lastResult.setValueToClosest(worker1.getValue());
								break;
			case RIGHT_SLANT:	if(worker0.getValue() > worker1.getValue())
									lastResult.setValueToClosest(worker0.getValue());
								else
									lastResult.setValueToClosest(worker1.getValue());
								break;
		}
	}

	@Override
	public Property getNext() {
		step();
		return getResult();
	}

	public DistributionScheme getMode(){
		return mode;
	}
}
