package Customs;

import model.ServiceServers;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class CustomSeekBar extends SeekBar implements OnSeekBarChangeListener{
	Handler hand;
	public CustomSeekBar(Context context, AttributeSet attrs) {
		super(context,attrs);
		setProgress(50);
		setOnSeekBarChangeListener(this);
	}
	public void setHandler(Handler hd){
		hand = hd;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
	
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		Log.i("Photo","wakeout");
		if (hand != null){
			Log.i("Photo","wakeup");
			hand.sendEmptyMessage(ServiceServers.STATUS_WAKEUP);
		}
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		Log.i("Photo","sleepout");
		setProgress(50);
		if (hand != null){
			Log.i("Photo","sleep");
			hand.sendEmptyMessage(ServiceServers.STATUS_SLEEP_COUNTS);
		}
		
	}
	
}
