package model;

import android.util.Log;

public class ControlUtil {
	int factorSpeedF = 15, factorSpeedS = 9;
	public int thGear = 1;
	public void setGear(int Gear) {
		thGear = Gear;
		switch (Gear) {
		case 1:
			factorSpeedF = 15;
			factorSpeedS = 9;
			break;
		case 2:
			factorSpeedF = 16;
			factorSpeedS = 12;
			break;
		case 3:
			factorSpeedF = 17;
			factorSpeedS = 18;
		}
	}
	
	
	public int calculateSpeed(int count){
//		return (int)((double) factorSpeedF
//				/(double)(1+Math.pow(3.6,-0.1*(count-50))) + factorSpeedS);
		int res = count - 50;
		if (res < 0)
			return (int)(-factorSpeedF * Math.pow(-res, 1.0 / 4.0) + 95);
		else 
			return (int)(factorSpeedS * Math.pow(res, 1.0 / 14.0) + 95);
	}
	
	public int calculateRotate(int count){
		int revers = 100 - count;
		return (int) (60 * ((double) revers / 100) + 60);
	}
	
	
	
}
