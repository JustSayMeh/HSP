package model;
 
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.example.clientmanagement.R;

public class SuperFragment extends Fragment{
	public RadioButton rfirst, rsecond, rthird;
	public Handler handler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public View.OnClickListener listenerRadio = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RadioButton th = (RadioButton) v;
			switch (th.getId()) {
			case R.id.firstB:
			case R.id.first:
				handler.sendEmptyMessage(ServiceServers.GEAR_ONE);
				break;
			case R.id.secondB:
			case R.id.second:
				handler.sendEmptyMessage(ServiceServers.GEAR_TWO);
				Log.i("Photo","two");
				break;
			case R.id.threeB:
			case R.id.three:
				handler.sendEmptyMessage(ServiceServers.GEAR_THREE);
				break;
			}

		}
	};
	
	public void setChecked(int d){
		switch(d){
		case 1: 
			rfirst.setChecked(true);
			
			break;
		case 2: 
			rsecond.setChecked(true);
			Log.i("Photo","tick");
			break;
		case 3: 
			rthird.setChecked(true);
			break;
		}
		rfirst.setOnClickListener(listenerRadio);
		rsecond.setOnClickListener(listenerRadio);
		rthird.setOnClickListener(listenerRadio);
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		if (handler != null)
			handler.sendEmptyMessage(ServiceServers.STATUS_CREATE_NOTIFICATION);
		
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		if (handler != null)
			handler.sendEmptyMessage(ServiceServers.STATUS_DESTROY_NOTIFICATION);
		
	}
	
	
}
