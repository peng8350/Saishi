package com.peng.saishi.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;

import com.peng.saishi.manager.App;

public class SystemUtils {

	/**
	 * 
	 * 判断activity是否存在
	 */
	@SuppressWarnings("deprecation")
	public static boolean isServiceRunning(String serviceClassName) {
		ActivityManager am = (ActivityManager) App.getInstance()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		boolean isAppRunning = false;
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(serviceClassName)
					|| info.baseActivity.getPackageName().equals(serviceClassName)) {
				isAppRunning = true;
				break;
			}
		}
		return isAppRunning;
	}

	/**
	 * 判断某个界面是否在前台
	 * 
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	@SuppressWarnings("deprecation")
	public static boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	// 得到宽
	@SuppressWarnings("deprecation")
	public static int getAppWidth() {
		WindowManager manager = (WindowManager) App.getInstance()
				.getSystemService(Context.WINDOW_SERVICE);
		return manager.getDefaultDisplay().getWidth();
	}

	// 得到宽
	@SuppressWarnings("deprecation")
	public static int getAppHeight() {
		WindowManager manager = (WindowManager) App.getInstance()
				.getSystemService(Context.WINDOW_SERVICE);
		return manager.getDefaultDisplay().getHeight()
				- getStatusHeight(App.getInstance());
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}
}
