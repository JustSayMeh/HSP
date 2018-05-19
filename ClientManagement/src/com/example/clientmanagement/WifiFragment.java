package com.example.clientmanagement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import model.CustTextWatcher;
import model.OptionsSaver;
import model.ServiceServers;
import model.ServiceServers.myBinder;
import model.SuperFragment;
import Customs.CustomSeekBar;
import Interfaces.ControlsInterface;
import Interfaces.RunnerInterface;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WifiFragment extends SuperFragment {
	private ToggleButton but;
	private EditText text, key;
	private CustomSeekBar sk, sk2;
	private DataOutputStream dt;
	static public String GlobalAddress;
	static private String TargetAddress;
	private CheckBox WhatAddress;
	private String type = "0";
	public static boolean isGoSleep = false;
	private Activity hostActivity;
	private ServiceServers service;
	private Socket socket;

	public WifiFragment() {
		// TODO Auto-generated constructor stub
	}

	private Handler activityHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			int res = msg.what;
			if (res == ServiceServers.STATUS_OK) {
				but.setChecked(true);
				return;
			}
			if (res == ServiceServers.STATUS_STOP) {
				stopNet();
				return;
			}
			String str = "Неизвестная ошибка";
			switch (res) {
			case ServiceServers.STATUS_CONNECTED:
				str = "Соединение установлено";
				Toast.makeText(hostActivity, str, Toast.LENGTH_LONG).show();
				return;
			case ServiceServers.STATUS_DICONNECTED:
				str = "Соединение разорвано";
				Log.i("Photo", str);
				stopNet();
				activityHandler.removeMessages(ServiceServers.ERR_SERVER_OFF);
				break;
			case ServiceServers.ERR_WRONG_FORMAT_IP:
				str = "Неверный формат ip";
				break;
			case ServiceServers.ERR_SERVER_NOT_AVAIBLE:
				str = "Сервер недоступен";
				break;
			case ServiceServers.ERR_SERVER_OFF:
				str = "Сервер отключен";
				break;
			case ServiceServers.ERR_WIFI_OFF:
				str = "Включите Wi-Fi";
				break;
			case ServiceServers.ERR_WRONG_FORMAT_KEY:
				str = "Введите ключ";
				break;
			}
			but.setChecked(false);
			Toast.makeText(hostActivity, str, Toast.LENGTH_LONG).show();

		}
	};

	public void onViewCreated(View view, Bundle savedInstanceState) {
		hostActivity = getActivity();
		key = (EditText) view.findViewById(R.id.key);
		but = (ToggleButton) view.findViewById(R.id.tvColor);
		text = (EditText) view.findViewById(R.id.editText);
		text.setMaxLines(1);
		sk = (CustomSeekBar) view.findViewById(R.id.seekBar1);
		sk2 = (CustomSeekBar) view.findViewById(R.id.sk2);
		rfirst = (RadioButton) view.findViewById(R.id.first);
		rsecond = (RadioButton) view.findViewById(R.id.second);
		rthird = (RadioButton) view.findViewById(R.id.three);

		text.addTextChangedListener(new CustTextWatcher(text,GlobalAddress));
		Intent i = new Intent(hostActivity, ServiceServers.class);
		hostActivity.bindService(i, connection, hostActivity.BIND_AUTO_CREATE);
		WhatAddress = (CheckBox) view.findViewById(R.id.checkBox);
		if (TargetAddress.equals(GlobalAddress)) {
			WhatAddress.setChecked(true);
			text.setEnabled(false);
		}
		WhatAddress.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					TargetAddress = GlobalAddress;
					text.setText(GlobalAddress);
					text.setEnabled(false);
				} else {
					TargetAddress = "";
					text.setEnabled(true);
					text.setText("");

				}
			}
		});

		text.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

				if (keyCode == 66) {
					TargetAddress = text.getText().toString();
					text.setText(TargetAddress.trim());
					text.clearFocus();

				}
				return false;
			}
		});
		OnClickListener butList = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!but.isChecked()) {
					handler.sendEmptyMessage(ServiceServers.STATUS_WAKEUP);
					handler.sendEmptyMessage(ServiceServers.STATUS_STOP);
					return;
				}
				but.setChecked(false);
				service.createRunner(new RunnerInterface() {
					@Override
					public OutputStream iniciation()
							throws UnknownHostException, IOException {
						if (key.getText().toString().trim().length() == 0)
							throw new UnknownError();
						if (TargetAddress.trim() == "")
							throw new UnknownHostException();
						socket = new Socket();
						socket.connect(
								new InetSocketAddress(TargetAddress.trim(),
										OptionsSaver.getPort()),
								1500);
						OutputStream sin = socket.getOutputStream();
						InputStream in = socket.getInputStream();
						dt = new DataOutputStream(sin);
						dt.writeUTF(key.getText().toString());
						dt.flush();
						activityHandler
								.sendEmptyMessage(ServiceServers.STATUS_OK);
						 DataInputStream oin = new DataInputStream(in);
						 if(oin.read() == 2)
							 activityHandler
								.sendEmptyMessage(ServiceServers.STATUS_DICONNECTED);
						 else
						// new EsterListener(oin, activityHandler);
							 activityHandler
									.sendEmptyMessage(ServiceServers.STATUS_CONNECTED);
						return dt;
					}
				});
			}
		};

		but.setOnClickListener(butList);
	};

	@Override
	public View onCreateView(LayoutInflater in, ViewGroup vg, Bundle s) {
		View view = in.inflate(R.layout.wifi_fragment, null);
		GlobalAddress = OptionsSaver.getAddr();
		TargetAddress = GlobalAddress;
		return view;

	}

	public void onDestroy() {
		super.onDestroy();
		Log.i("Photo", "destroy");
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
					return sk2.getProgress();
				}

				@Override
				public int readRotate() {
					return sk.getProgress();
				}
			});
			handler = service.handler(activityHandler);
			sk.setHandler(handler);
			sk2.setHandler(handler);
			setChecked(service.getGear());
		}
	};

	private void stopNet() {
		try {

			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		hostActivity.unbindService(connection);
	}

}
