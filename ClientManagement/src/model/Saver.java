package model;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Saver {
	public static void saveDevice(BluetoothDevice device,Activity activity){
		SharedPreferences p =  activity.getPreferences(activity.MODE_PRIVATE);
		Editor edit = p.edit();
		edit.putString(device.getAddress(), device.getName());
		edit.commit();
	}
	
	
	
	public static boolean load(Activity activity,BluetoothDevice device){
		SharedPreferences p =  activity.getPreferences(activity.MODE_PRIVATE);
		String res = p.getString(device.getAddress(),"");
		if (res.length() == 0) return false;
		return true;
	}
	
	public static void remove(Activity activity,BluetoothDevice device){
		SharedPreferences p =  activity.getPreferences(activity.MODE_PRIVATE);
		Editor edit = p.edit();
		edit.remove(device.getAddress()).commit();
		
	}
}
