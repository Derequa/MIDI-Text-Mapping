package gernerators.properties;

import java.util.Random;

public class Organization extends Property implements Comparable<Organization> {
	
	public enum OrgMode{
		MICRO,
		MACRO
	}
	
	private static final int MAX_SHIFT = 7;
	//private static final float MODE_SHIFT_PROB = .4f;
	public static final int[] SHIFTS = {0, 4, 7, -4, -7, 3 , -3};
	public static final int IDX_NO_CHANGE = 0;
	public static final int IDX_UP_CONT0 = 1;
	public static final int IDX_UP_CONT1 = 2;
	public static final int IDX_DWN_CONT0 = 3;
	public static final int IDX_DWN_CONT1 = 4;
	public static final int IDX_UP_CHNG0 = 5;
	public static final int IDX_DWN_CHNG0 = 6;
	
	private OrgMode mode;
	private Random r = new Random();

	public Organization(OrgMode mode) {
		super(0);
		setMode(mode);
	}
	
	public OrgMode getMode(){
		return mode;
	}
	
	public void setMode(OrgMode newMode){
		if((newMode == OrgMode.MICRO) || (newMode == OrgMode.MACRO))
			mode = newMode;
		else
			throw new IllegalArgumentException("INVALID ORGANIZATION MODE");
	}

	@Override
	public void randomize() {
		switch(mode){
			case MACRO:		setValueToClosest(r.nextInt(17) - 8);
							break;
			case MICRO:		setValueToClosest(r.nextInt(MAX_SHIFT));
							break;
			default:		break;
		}
		
	}

	@Override
	public void setValueToClosest(int newValue) {
		switch(mode){
		case MACRO:		value = newValue;
						break;
		case MICRO:		int minDist = Integer.MAX_VALUE;
						int defaultIndex = IDX_NO_CHANGE;
						for(int i = 0 ; i < SHIFTS.length ; i++){
							if(Math.abs(newValue - SHIFTS[i]) < minDist){
								defaultIndex = i;
								minDist = Math.abs(newValue - SHIFTS[i]);
							}
						}
						value = SHIFTS[defaultIndex];
						break;
		default:		break;
		}
		
	}

	@Override
	public int compareTo(Organization arg0) {
		return (new Integer(value)).compareTo(new Integer(arg0.getValue()));
	}

}
