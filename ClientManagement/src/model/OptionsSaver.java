package model;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OptionsSaver {
	private static final int defPort = 8080;
	private static final String defAddr = "109.174.13.39";
	private static final int defTimeout = 20;
	private static int curPort = -1, curTimeout = -1;
	private static String curAddr;
	public static void init(Activity ac){
		SharedPreferences pr = ac.getSharedPreferences("options", Activity.MODE_PRIVATE);
		curPort = pr.getInt("port", defPort);
		curAddr = pr.getString("adr", defAddr);
		curTimeout = pr.getInt("timeout", defTimeout);
	}
	public static void save(Activity ac){
		SharedPreferences pr = ac.getSharedPreferences("options", Activity.MODE_PRIVATE);
		Editor ed = pr.edit();
		ed.putInt("port", curPort);
		ed.putString("adr", curAddr);
		ed.putInt("timeout", curTimeout);
		ed.commit();
	}
	
	public static int getPort(){
		return curPort;
	}
	
	public static int getTimeout(){
		return curTimeout;
	}
	
	public static String getAddr(){
		return curAddr;
	}
	
	public static void setPort(int p){
		curPort = p;
	}
	
	public static void setTimeout(int t){
		curTimeout = t;
	}
	
	public static void setAddr(String addr){
		curAddr = addr;
	}
	
}
