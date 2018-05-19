package com.example.clientmanagement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.UUID;

import model.ServiceServers;
import model.ServiceServers.myBinder;
import model.SuperFragment;
import Interfaces.ControlsInterface;
import Interfaces.RunnerInterface;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class BlueToothFragment extends SuperFragment {
	public static String NAME = "name", ADRESS = "adress";
	private SeekBar speed, rule;
	private BluetoothSocket socket;
	private Activity hostActivity;
	private ServiceServers service;
	private String addr = "";
	private Handler activityHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			int res = msg.what;
			String str = "Неизвестная ошибка";
			switch (res) {
			case ServiceServers.STATUS_CONNECTED:
				str = "Соединение установлено";
				break;
			case ServiceServers.ERR_SERVER_NOT_AVAIBLE:
				str = "Сервер недоступен";
			case ServiceServers.STATUS_STOP:
				close();
				break;
			case ServiceServers.ERR_SERVER_OFF:
				str = "Соединение разорвано";
				close();
				break;
			}

			Toast.makeText(hostActivity, str, Toast.LENGTH_LONG).show();

		}
	};
	
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Bundle args = getArguments();
		addr = args.getString(ADRESS).trim();
		EditText edit = (EditText) view.findViewById(R.id.adress);
		speed = (SeekBar) view.findViewById(R.id.speed);
		rule = (SeekBar) view.findViewById(R.id.rule);
		rfirst = (RadioButton) view.findViewById(R.id.firstB);
		rsecond = (RadioButton) view.findViewById(R.id.secondB);
		rthird = (RadioButton) view.findViewById(R.id.threeB);
		hostActivity = getActivity();
		Intent i = new Intent(hostActivity, ServiceServers.class);
		hostActivity.bindService(i, connection, hostActivity.BIND_AUTO_CREATE);
		edit.setText(args.getString(NAME));
	};

	@Override
	public View onCreateView(LayoutInflater in, ViewGroup vg, Bundle s) {
		View view = in.inflate(R.layout.main_bluetooth_fragment, null);
		EditText edit = (EditText) view.findViewById(R.id.adress);
		return view;
	}

	ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {

		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			myBinder d = (myBinder) arg1;
			service = d.getService();
			service.setControls(new ControlsInterface() {
				@Override
				public int readSpeed() {
					return speed.getProgress();
				}

				@Override
				public int readRotate() {
					return rule.getProgress();
				}
			});

			handler = service.handler(activityHandler);
			setChecked(service.getGear());

			service.createRunner(new RunnerInterface() {
				@Override
				public OutputStream iniciation() throws ConnectException,
						IOException {
					BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
					BluetoothDevice device = ba.getRemoteDevice(addr);
					socket = device.createRfcommSocketToServiceRecord(UUID
							.fromString(BlueToothList.SDP_KEY));
					socket.connect();
					InputStream out = socket.getInputStream();
					out.read();
					activityHandler.sendEmptyMessage(ServiceServers.STATUS_CONNECTED);
					return socket.getOutputStream();
				}
			});

		}
	};

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hostActivity.getFragmentManager().popBackStack();
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.i("Photo","detach");
		hostActivity.unbindService(connection);
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
