package com.example.clientmanagement;

import model.OptionsSaver;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RemoteViews;

public class MainActivity extends Activity {
	public static DrawerLayout drawerLayout;
	public static FragmentManager fm;

	protected void onCreate(Bundle save) {
		super.onCreate(save);
		OptionsSaver.init(this);
		setContentView(R.layout.activity_main);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		fm = getFragmentManager();
		if (fm.findFragmentById(R.id.container) == null){
			fm.beginTransaction().add(R.id.container, new WifiFragment()).commit();
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	protected void onPause() {
		Log.i("Photo","pauseActivity");
		super.onPause();
	}
}
