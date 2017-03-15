package com.peng.saishi.receiver;

import com.peng.saishi.interfaces.observer.AlarmListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
 * 
 * 接受到闹钟的接受者
 */
public class MyAlarmReceiver extends BroadcastReceiver {
	
	private AlarmListener listener;
	
	public AlarmListener getListener() {
		return listener;
	}

	public void setListener(AlarmListener listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//接受到了
		if (listener!=null) {
			listener.onReceiveAlarm(intent);
		}
		
	}

}
