package com.peng.saishi.receiver;

import com.peng.saishi.manager.AppManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * App程序的广播接受者
 * @author peng
 *
 */
public class ExitReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		AppManager.ExitApp();
	}

}
