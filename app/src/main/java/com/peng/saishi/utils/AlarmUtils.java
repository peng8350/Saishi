package com.peng.saishi.utils;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.peng.saishi.activity.MainActivity;
import com.peng.saishi.entity.MatchInfo;

/**
 * 
 * 闹钟的工具类
 * @author peng
 *
 */
public class AlarmUtils {
	/**
	 * 比赛开始的前xx天开始,给用户提示
	 * 30,15,5
	 */
	
	public static void ShowTipsforMatch(Context context,MatchInfo info){
	    // When the alarm goes off, we want to broadcast an Intent to our
        // BroadcastReceiver. Here we make an Intent with an explicit class
        // name to have our own receiver (which has been published in
        // AndroidManifest.xml) instantiated and called, and then create an
        // IntentSender to have the intent executed as a broadcast.
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("match", info);
        
        PendingIntent sender = PendingIntent.getBroadcast(
               context, 0, intent, 0);

        // We want the alarm to go off 10 seconds from now.
        //不同时间端
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}

}
