package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import Interfaces.ControlsInterface;
import Interfaces.RunnerInterface;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ServiceServers extends Service {
	public static final int STATUS_OK = 0;
	public static final int GEAR_ONE = 1;
	public static final int GEAR_TWO = 2;
	public static final int GEAR_THREE = 3;

	public static final int ERR_WRONG_FORMAT_IP = 4;
	public static final int ERR_WRONG_FORMAT_KEY = 5;
	public static final int ERR_WIFI_OFF = 6;
	public static final int ERR_SERVER_OFF = 7;
	public static final int ERR_CLIENT_OFF = 8;
	public static final int ERR_SERVER_NOT_AVAIBLE = 9;

	public static final int STATUS_STOP = 10;
	public static final int STATUS_SLEEP = 11;
	public static final int STATUS_WAKEUP = 12;
	public static final int STATUS_CREATE_NOTIFICATION = 13;
	public static final int STATUS_DESTROY_NOTIFICATION = 14;
	public static final int STATUS_CONNECTED = 15;
	public static final int STATUS_DICONNECTED = 16;
	public static final int STATUS_SLEEP_COUNTS = 17;
	
	private RunnerInterface runner;
	private ControlUtil util = new ControlUtil();
	private volatile ControlsInterface controls;
	private DataOutputStream dt;
	public String h = "1";
	private final int timeout = OptionsSaver.getTimeout();
	private volatile boolean isWork = true;
	private Handler activityHandler;
	private ServerThread thread;
	private int cou = 0;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GEAR_ONE:
			case GEAR_TWO:
			case GEAR_THREE:
				util.setGear(msg.what);
				break;
			case STATUS_SLEEP_COUNTS:
				if (--cou > 0){
					Log.i("Photo",cou+"sd");
					break;
				}
			case STATUS_SLEEP:
				if (thread == null)
					return;
				thread.pause();
				cou = 0;
				break;
			case STATUS_WAKEUP:
				if (thread == null)
					return;
				thread.resumeWork();
				cou++;
				break;
			case STATUS_CREATE_NOTIFICATION:
				startNotification();
				break;
			case STATUS_DESTROY_NOTIFICATION:
				stopNotification();
				break;
			case STATUS_STOP:
				cou = 0;
				isWork = false;
				thread = null;
				break;

			}
		}
	};
	private NotificationWorker noti;
	public ControlsInterface getControlsInterface() {
		return controls;
	}

	public class myBinder extends Binder {
		public ServiceServers getService() {
			
			return ServiceServers.this;
		}
	}

	private void startNotification() {
		if (thread == null)
			return;
		Log.i("Photo","sd");
		thread.pause();
		noti.buildNotification();

	}

	private void stopNotification() {
		noti.releaseNotification();
		//handler.sendEmptyMessage(STATUS_WAKEUP);
	}

	public Handler handler(Handler h) {
		activityHandler = h;
		noti = new NotificationWorker(this, handler);
		return handler;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new myBinder();
	}

	public void setControls(ControlsInterface con) {
		if (con == null)
			return;
		controls = con;
	}

	public boolean createRunner(RunnerInterface run) {
		thread = new ServerThread(run);
		return true;
	}

	public int getGear() {
		return util.thGear;
	}

	@Override
	public void onDestroy() {
		isWork = false;
	}

	class ServerThread extends Thread {
		private volatile boolean stopWork = true;
		public ServerThread(RunnerInterface run) {
			runner = run;
			start();
		}
		public synchronized void resumeWork() {
			notifyAll();
			stopWork = false;
		}
		public void pause() {
			stopWork = true;
		}

		@Override
		public void run() {
			try {
				dt = new DataOutputStream(runner.iniciation());
				String res = "";
				int proc, powerProc;
				isWork = true;
				while (isWork) {
					while (stopWork) {
						dt.writeUTF("952.951.");
						synchronized (this) {
							this.wait();
						}

					}
//					if (cpp > 23) {
//						sleep(290);
//						cpp = 0;
//					}

//					cpp++;
					proc = util.calculateRotate(controls.readRotate());
					powerProc = util.calculateSpeed(controls.readSpeed());
					StringBuilder builder = new StringBuilder();
					builder.append(String.valueOf(proc));
					builder.append("2");
					builder.append(".");
					builder.append(String.valueOf(powerProc));
					builder.append("1");
					builder.append(".");
					res = builder.toString();
					dt.writeUTF(res);
					dt.flush();
					sleep(timeout);
				}
				Log.i("Photo", isWork + "");
				activityHandler.sendEmptyMessage(STATUS_STOP);
			} catch (UnknownError e) {
				activityHandler.sendEmptyMessage(ERR_WRONG_FORMAT_KEY);
			} catch (ConnectException e) {
				Log.i("Photo", e.toString());
				if (!((WifiManager) getSystemService(Context.WIFI_SERVICE))
						.isWifiEnabled())
					activityHandler.sendEmptyMessage(ERR_WIFI_OFF);
				else
					activityHandler.sendEmptyMessage(ERR_SERVER_NOT_AVAIBLE);
			} catch (UnknownHostException e) {
				activityHandler.sendEmptyMessage(ERR_WRONG_FORMAT_IP);
			} catch (SocketTimeoutException e) {
				activityHandler.sendEmptyMessage(ERR_SERVER_NOT_AVAIBLE);
			} catch (IOException e) {
				String str = (e.getMessage() == null) ? e.toString() : e
						.getMessage();
				switch (str) {
				case "Broken pipe":
					activityHandler
							.sendEmptyMessage(ServiceServers.ERR_SERVER_OFF);
					break;
				case "read failed, socket might closed or timeout, read ret: -1":
					activityHandler
							.sendEmptyMessage(ServiceServers.ERR_SERVER_NOT_AVAIBLE);
					break;
				default:
					activityHandler.sendEmptyMessage(ERR_SERVER_OFF);
				}

			} catch (InterruptedException e) {

			} finally {
				noti.releaseNotification();
				handler.sendEmptyMessage(STATUS_STOP);
			}
		}
	}

}
