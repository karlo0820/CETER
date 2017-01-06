package com.jiale.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NetWorkStateService extends Service {
	private final static String TAG = "NETWORKSTATE";
	private NetworkInfo mobileInfo;
	private NetworkInfo wifiInfo;
	private NetWorkState netWorkState;
	private MyBinder binder = new MyBinder();
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.i("SERVICE", action);
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				boolean isMobile = isMobileNetWork(context);
				boolean isWiFi = isWiFiNetWork(context);
				System.out.println("isMobile------>" + isMobile);
				System.out.println("isWiFi------>" + isWiFi);
				// 1 只有3G 2 没有连接
				if (netWorkState != null) {
					if (isMobile && !isWiFi) {
//						Toast.makeText(context, "当前网络为非WIFI状态,可能会产生额外流量损耗", Toast.LENGTH_LONG).show();
						Log.i(TAG, 1 + "");
						netWorkState.getState(1);
					} else if (!isMobile && !isWiFi) {
//						Toast.makeText(context, "无网络连接，请连接网络", Toast.LENGTH_LONG).show();
						netWorkState.getState(2);
						Log.i(TAG, 2 + "");
					}
				} else {
					Log.i("Service", "netWorkState--->null");
				}
			}
		}

	};

	public class MyBinder extends Binder {
		public NetWorkStateService getService() {
			return NetWorkStateService.this;
		}
	}
	public interface NetWorkState {
		public void getState(int flat);
	}
	public void setNetWorkState(NetWorkState netWorkState) {
		this.netWorkState = netWorkState;
	}

	private boolean isMobileNetWork(Context context) {
		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectManager != null) {

			mobileInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobileInfo != null && mobileInfo.isConnected()) {
				return true;
			}
		}
		return false;
	}

	private boolean isWiFiNetWork(Context context) {
		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectManager != null) {
			wifiInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiInfo != null && wifiInfo.isConnected()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		Log.i("SERVICE", "Create");
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, mFilter);
		Log.i("SERVICE", "注册");
	}

	@Override
	public void onDestroy() {
		Log.i("SERVICE", "DESTORY");
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("SERVICE", "start");
		return super.onStartCommand(intent, flags, startId);
	}

}
