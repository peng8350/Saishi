package com.peng.saishi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.peng.saishi.interfaces.observer.NetworkListener;

public class NetworkReceiver extends BroadcastReceiver {

	
	private NetworkListener listener;

	@SuppressWarnings("deprecation")
	public void onReceive(final Context context, final Intent intent) {

		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo networkInfo = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (networkInfo.isConnected()) {
				// Wifi is connected
				if (listener!=null) {
					listener.onConnect();
				}
			}
		} else if (intent.getAction().equals(
				
				ConnectivityManager.CONNECTIVITY_ACTION)) {
			NetworkInfo networkInfo = intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
					&& !networkInfo.isConnected()) {
				System.out.println("失去连接");
				// Wifi is disconnected
				listener.onDisConnect();
			}
		}
	}

	public NetworkListener getListener() {
		return listener;
	}

	public void setListener(NetworkListener listener) {
		this.listener = listener;
	}

}
