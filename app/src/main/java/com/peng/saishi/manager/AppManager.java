package com.peng.saishi.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 
 * @author by peng 用来管理activity的添加，销毁 一次性摧毁所有的activity
 */
public class AppManager {

	// 存放
	public static List<Activity> activities;

	public static List<Activity> getInstance() {
		if (activities == null) {

			activities = new ArrayList<Activity>();

		}
		
		return activities;

	}

	public static void AddActivity(Activity act) {

		getInstance().add(act);

	}

	public static void RemoveAct(Activity act) {

		getInstance().remove(act);

	}

	// 退出ＡＰＰ,销毁所有的activity
	public static void ExitApp() {

		for (Activity act : activities) {

			act.finish();
			
		}

	}
	
	// 退出ＡＰＰ,销毁所有的activity
	public static void FinishAllAct() {

		for (Activity act : activities) {

			act.finish();

		}


	}
}
