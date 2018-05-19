package model;

import Interfaces.ControlsInterface;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.clientmanagement.R;

public class NotificationWorker extends BroadcastReceiver implements
		ControlsInterface {
	private static RemoteViews view;
	private static Notification not;
	private static ServiceServers service;
	private ControlsInterface oldcontrol;
	private CustSensorListener sens;
	private static Handler handler;
	private int NUMBER = 92;
	public NotificationWorker(){
	}
	public NotificationWorker(ServiceServers ser, Handler hand) {
		service = ser;
		handler = hand;
		sens = new CustSensorListener();
		not = new Notification.Builder(ser)
				.setContentTitle("Уведомление активно")
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentText("Его текст").build();
		oldcontrol = ser.getControlsInterface();
	}

	public void buildNotification() {
		SensorManager manager = (SensorManager) service
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager.registerListener(sens, sensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		view = new RemoteViews(service.getPackageName(),
				R.layout.notification_view);
		view.setOnClickPendingIntent(R.id.notId,
				getPending(service, "AchOn"));
		view.setTextViewText(R.id.notId, service.getResources().getString(R.string.On));
		not.contentView = view;
		service.startForeground(NUMBER, not);
		oldcontrol = service.getControlsInterface();
		service.setControls(this);
	}

	public void releaseNotification() {
		SensorManager manager = (SensorManager) service
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager.unregisterListener(sens, sensor);
		service.setControls(oldcontrol);
		service.stopForeground(true);

	}

	private PendingIntent getPending(Context con, String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		return PendingIntent.getBroadcast(con, 0, intent, 0);
	}

	@Override
	public int readSpeed() {
		int speed = sens.getSpeed();
		Log.i("Photo",speed+"");
		if (speed > 85)
			speed = 85;
		if (speed < -85)
			speed = -85;
		speed = 100 - (int) ((speed + 90) / 1.8);
		return speed;
	}

	@Override
	public int readRotate() {
		int deg = sens.getDeg();
		if (deg > 85)
			deg = 85;
		if (deg < -85)
			deg = -85;
		deg = 100 - (int) ((deg + 90) / 1.8);
		return deg;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String i = "";
		String text = "";
		switch (intent.getAction()){
		case "AchOn":
			handler.sendEmptyMessage(ServiceServers.STATUS_WAKEUP);
			i = "AchOff";
			text = context.getResources().getString(R.string.Off);;
			break;
		case "AchOff":
			handler.sendEmptyMessage(ServiceServers.STATUS_SLEEP);
			i ="AchOn";
			text = context.getResources().getString(R.string.On);;
			break;
		}
		view.setOnClickPendingIntent(R.id.notId,
				getPending(service, i));
		view.setTextViewText(R.id.notId, text);
		not.contentView = view;
		manager.notify(NUMBER, not);
		
		
	}
}
