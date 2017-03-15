package com.peng.saishi.utils;

import com.peng.saishi.manager.App;

import android.widget.Toast;

/**
 * 这个工具类是拿来显示toast信息
 * @author peng
 *
 */
public class ToastUtils {

	
	//显示短信息的toast
	public static void ShowShortToast(String content){
		Toast.makeText(App.getInstance(), content, 0).show();
	}
	
	//显示长信息的toast
	public static void ShowLongToast(String content){
		Toast.makeText(App.getInstance(), content, 1).show();
	}
	//显示网络异常
	public static void ShowNetError(){
		Toast.makeText(App.getInstance(), "网络异常,请检查网络!", 0).show();
	}
	
	
}
