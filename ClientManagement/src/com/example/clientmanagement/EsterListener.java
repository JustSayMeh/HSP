package com.example.clientmanagement;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import model.ServiceServers;
import android.os.Handler;
import android.util.Log;

public class EsterListener extends Thread{
	private DataInputStream oin;
	private Socket socket;
	private Handler handler;
	public EsterListener(DataInputStream in,Handler handler){
		oin = in;
		this.handler = handler;
		Log.i("Photo","init");
		start();
	}
	
	
	public void run(){
		String res;
		try {
			res = oin.readUTF();
			if (res.equals("12")){
				WifiFragment.isGoSleep = true;
				handler.sendEmptyMessage(ServiceServers.STATUS_DICONNECTED);
			}
			else new EsterListener(oin,handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
