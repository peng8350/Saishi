package com.peng.saishi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by fan on 2016/6/23.
 */
public class TimeUtils {

	// 毫秒转秒
	public static String long2String(long time) {

		// 毫秒转秒
		int sec = (int) time / 1000;
		int min = sec / 60; // 分钟
		sec = sec % 60; // 秒
		if (min < 10) { // 分钟补0
			if (sec < 10) { // 秒补0
				return "0" + min + ":0" + sec;
			} else {
				return "0" + min + ":" + sec;
			}
		} else {
			if (sec < 10) { // 秒补0
				return min + ":0" + sec;
			} else {
				return min + ":" + sec;
			}
		}

	}

	/**
	 * 返回当前时间的格式为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		return sdf.format(System.currentTimeMillis());
	}

	/*
	 * 
	 * 从一个String中获得date
	 */
	public static Date getDateFromStr(String date_str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		try {
			return format.parse(date_str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取通知时间
	 * @param date_str
	 * @return
	 */
	public static String getNoticeTime(Date date){

		  SimpleDateFormat formatter = new SimpleDateFormat("MM-dd\n\nHH:mm");
		  formatter.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		  String dateString = formatter.format(date);
		  return dateString;
		
	}
	
	/*
	 * 
	 * 从一个String中获得date
	 */
	public static Date getDateFromChatTime(String date_str) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		try {
			return format.parse(date_str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//判断不在两分钟之内
	public static boolean isTwoMinute(Date date1,Date date2){
		if (Math.abs(date1.getTime()-date2.getTime())>60000){
			return false;
		}
		return true;
	}

	/**  * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss  *   * @param dateDate  * @return  */
	public static String dateToStrLong(java.util.Date dateDate) {
	  SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
	  formatter.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
	  String dateString = formatter.format(dateDate);
	  return dateString;
	}
}
