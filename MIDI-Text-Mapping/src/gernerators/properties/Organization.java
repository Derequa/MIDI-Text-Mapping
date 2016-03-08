package gernerators.properties;

import java.util.Random;

public class Organization extends Property implements Comparable<Organization> {
	
	public enum OrgMode{
		MICRO,
		MACRO
	}
	
	private static final int MAX_SHIFT = 7;
	//private static final float MODE_SHIFT_PROB = .4f;
	private static final int[] SHIFTS = {0, 4, 7, -4, -7, 3 , -3};
	private static final int IDX_NO_CHANGE = 0;
	private static final int IDX_UP_CONT0 = 1;
	private static final int IDX_UP_CONT1 = 2;
	private static final int IDX_DWN_CONT0 = 3;
	private static final int IDX_DWN_CONT1 = 4;
	private static final int IDX_UP_CHNG0 = 5;
	private static final int IDX_DWN_CHNG0 = 6;
	
	private OrgMode mode;
	private Random r = new Random();
	
	// Value is only shift
	private int centralPitch;
	
	public Organization(OrgMode mode){
		this(mode, 0);
	}

	public Organization(OrgMode mode, int centralPitch) {
		super(centralPitch);
		this.centralPitch = centralPitch;
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
		setValueToClosest(r.nextInt(MAX_SHIFT));
	}

	@Override
	public void setValueToClosest(int newValue) {
		int minDist = Integer.MAX_VALUE;
		int defaultIndex = IDX_NO_CHANGE;
		for(int i = 0 ; i < SHIFTS.length ; i++){
			if(Math.abs(newValue - SHIFTS[i]) < minDist){
				defaultIndex = i;
				minDist = Math.abs(newValue - SHIFTS[i]);
			}
		}
		value = SHIFTS[defaultIndex];
	}

	@Override
	public int compareTo(Organization arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
