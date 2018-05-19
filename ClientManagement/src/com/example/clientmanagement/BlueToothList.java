package com.example.clientmanagement;

import java.util.ArrayList;
import java.util.Set;

import model.Saver;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlueToothList extends ListFragment {
	private static BluetoothAdapter ba;
	private ListView listview;
	private CustAdapter adapter;
	private final int RESULT_BLUETOOTH = 89;
	private ArrayList<BluetoothDevice> devices;
	public static final String SDP_KEY = "c157d5ed-493a-4a55-9425-b0549223b9ad";

	public BlueToothList() {
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityResult(int req, int res, Intent data) {
		Log.i("Photo", req + "");
		if (req != RESULT_BLUETOOTH || !ba.isEnabled()) {
			try {
				getActivity()
						.getFragmentManager()
						.beginTransaction()
						.replace(R.id.container,
								WifiFragment.class.newInstance()).commit();
			} catch (java.lang.InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		Set<BluetoothDevice> mass = ba.getBondedDevices();
		for (BluetoothDevice th : mass) {
			if (Saver.load(getActivity(), th))
				devices.add(th);

		}
		adapter = new CustAdapter();
		listview.setAdapter(adapter);

	}

	public View onCreateView(LayoutInflater in, ViewGroup vg, Bundle s) {
		Log.i("Photo","createView");
		View view = in.inflate(R.layout.bluetooth_fragment, null);
		listview = (ListView) view.findViewById(android.R.id.list);
		registerForContextMenu(listview);
		if (devices == null){
			devices = new ArrayList<BluetoothDevice>();
			activateBlueTooth();
			}
		
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	class CustAdapter extends ArrayAdapter<BluetoothDevice> {

		public CustAdapter() {
			super(getActivity(), 0, devices);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int pos, View v, ViewGroup vg) {
			if (v == null) {
				v = getActivity().getLayoutInflater().inflate(
						R.layout.bluetooth_item, null);

			}
			
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView adress = (TextView) v.findViewById(R.id.adress);
			BluetoothDevice th = devices.get(pos);
			String nameS = th.getName();
			String adressS = th.getAddress();
			name.setText(getResources().getString(R.string.BluetoothName)
					+ nameS);
			adress.setText(getResources().getString(R.string.BluetoothAdress)
					+ adressS);
			return v;
		}

	}

	@Override
	public void onListItemClick(ListView i, View v, int pos, long id) {
		Bundle args = new Bundle();
		args.putString(BlueToothFragment.NAME, devices.get(pos).getName());
		args.putString(BlueToothFragment.ADRESS, devices.get(pos).getAddress());
		Fragment th = new BlueToothFragment();
		th.setArguments(args);
		getActivity().getFragmentManager().beginTransaction()
				.replace(R.id.container, th).addToBackStack(null).commit();
	}

	private void activateBlueTooth() {
		ba = BluetoothAdapter.getDefaultAdapter();
		Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(i, RESULT_BLUETOOTH);

	}

	static class alertView {
		private static AlertDialog ad;
		private static BluetoothDevice device;

		public static void show(final Activity c) {
			View v = c.getLayoutInflater().inflate(R.layout.alert_window, null);
			Button but = (Button) v.findViewById(R.id.okButoon);
			final EditText text = (EditText) v.findViewById(R.id.enter);

			ad = new AlertDialog.Builder(c).setView(v)
					.setTitle(c.getResources().getString(R.string.alertTitle))
					.create();
			but.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BlueToothFragment bf = new BlueToothFragment();

					String addr = text.getText().toString();
					try {
						device = ba.getRemoteDevice(addr);
					} catch (IllegalArgumentException e) {
						Toast.makeText(c, "неверный формат адреса", 1500)
								.show();
						text.setText("");
						return;
					} catch (Exception e) {
						Log.i("Photo", e.getMessage());
					}
					Saver.saveDevice(device, c);
					Bundle args = new Bundle();
					args.putString(BlueToothFragment.NAME, device.getName());
					args.putString(BlueToothFragment.ADRESS,
							device.getAddress());
					Fragment th = new BlueToothFragment();
					th.setArguments(args);
					ad.dismiss();
					c.getFragmentManager().beginTransaction()
							.replace(R.id.container, th).addToBackStack(null)
							.commit();
				}
			});
			ad.show();

		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int pos = info.position;
		BluetoothDevice remDevice = devices.remove(pos);
		Saver.remove(getActivity(), remDevice);
		adapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, android.R.id.list, 0, "Удалить");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onCreateOptionsMenu(Menu m, MenuInflater mi) {
		mi.inflate(R.menu.bluetooth_menu, m);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem i) {
		switch (i.getItemId()) {
		case R.id.push:
			alertView.show(getActivity());
			break;
		}
		return true;
	}

}
