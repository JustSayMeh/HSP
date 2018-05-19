package model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class CustSensorListener implements SensorEventListener{
	private volatile int Deg = 0;
	private volatile int Speed = 0;
	public int getDeg(){
		return Deg;
	}
	
	 public int getSpeed() {
		return Speed;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		Deg = (int) (event.values[2])*10;
		Speed = (int) (event.values[0])*10;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
}
