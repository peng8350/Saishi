package com.peng.saishi.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.ipaulpro.afilechooser.FileChooserActivity;
import com.peng.saishi.exception.lenequalsException;

/**
 * Activity跳转的工具类
 * 
 * @author peng 1.带Int的跳转 2.带String的跳转 3.带多个int的跳转 4.带多个String的跳转
 *         5.带多个int,多个String的跳转 6.没有任何要求的跳转
 */
public class ActChangeUtils {
	
	//打开文件选择
	public static void OpenFileChoose(Context context,int request){
		Intent intent = new Intent(context, FileChooserActivity.class);
		((Activity)context).startActivityForResult(intent, request);
	}

	public static void ChangeActivity(Activity context, Class<? extends Activity> act) {
		Intent intent = new Intent(context, act);
		context.startActivity(intent);
	}

	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String key, String value) {
		Intent intent = new Intent(context, act);
		intent.putExtra(key, value);
		context.startActivity(intent);
	}

	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String key, Parcelable[] values) {
		Intent intent = new Intent(context, act);
		intent.putExtra(key, values);
		context.startActivity(intent);
	}
	
	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String key, Parcelable values) {
		Intent intent = new Intent(context, act);
		intent.putExtra(key, values);
		context.startActivity(intent);
	}
	
	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String key, List<? extends Parcelable> values) {
		Intent intent = new Intent(context, act);
		intent.putParcelableArrayListExtra(key, (ArrayList<? extends Parcelable>) values);
		context.startActivity(intent);
	}

	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String key, Serializable value) {
		Intent intent = new Intent(context, act);
		intent.putExtra(key, value);
		context.startActivity(intent);
	}

	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String key, int value) {
		Intent intent = new Intent(context, act);
		intent.putExtra(key, value);
		context.startActivity(intent);
	}
	
	public static void ChangeActivityForResult(Activity context, Class<? extends Activity> act, String key, int value,int code) {
		Intent intent = new Intent(context, act);
		intent.putExtra(key, value);
		((Activity)context).startActivityForResult(intent,101);
	}
	public static void ChangeActivityForResult(Activity context, Class<? extends Activity> act, String key, Parcelable value,int code) {
		Intent intent = new Intent(context, act);
		intent.putExtra(key, value);
		((Activity)context).startActivityForResult(intent,101);
	}
	public static void ChangeActivityForResult(Activity context, Class<? extends Activity> act, String key, Serializable value,int code) {
		Intent intent = new Intent(context, act);
		intent.putExtra(key, value);
		((Activity)context).startActivityForResult(intent,101);
	}
	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String[] key, String[] value) {
		Intent intent = new Intent(context, act);
		if (key.length != value.length) {
			try {
				throw new lenequalsException("you should match the length of the key array and value array!!!!");
			} catch (lenequalsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < key.length; i++)
			intent.putExtra(key[i], value[i]);
		context.startActivity(intent);
	}

	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String[] key, int[] value) {
		Intent intent = new Intent(context, act);
		if (key.length != value.length) {
			try {
				throw new lenequalsException("you should match the length of the key array and value array!!!!");
			} catch (lenequalsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < key.length; i++)
			intent.putExtra(key[i], value[i]);
		context.startActivity(intent);
	}

	public static void ChangeActivity(Activity context, Class<? extends Activity> act, String[] key, String id,int type) {
		Intent intent = new Intent(context, act);
		intent.putExtra("id", id);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}
	public static void ChangeActivityForResult(Activity context, Class<? extends Activity> act, int requestcode) {
		Intent intent = new Intent(context, act);
		context.startActivityForResult(intent, requestcode);
	}

	public static void ChangeActivityforResult(Activity context, Class<? extends Activity> act, String[] key,
			String[] value, int requestcode) {
		Intent intent = new Intent(context, act);
		if (key.length != value.length) {
			try {
				throw new lenequalsException("you should match the length of the key array and value array!!!!");
			} catch (lenequalsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < key.length; i++) {
			intent.putExtra(key[i], value[i]);
		}
		context.startActivityForResult(intent, requestcode);
	}

}
