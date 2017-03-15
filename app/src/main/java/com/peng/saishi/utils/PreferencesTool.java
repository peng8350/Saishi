package com.peng.saishi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.peng.saishi.exception.lenequalsException;
import com.peng.saishi.manager.App;

/**
 * 方便操作sharedpreferences 1.放多个值 2.仿一个值 3.得到一个值
 * 
 * @author peng
 *
 */
public class PreferencesTool {

	public static void setParams(String filename, String[] key, Object[] value) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(filename, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		if (key.length != value.length) {
			try {
				throw new lenequalsException("you should match the length of the key array and value array!!!!");
			} catch (lenequalsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < key.length; i++) {
			if (value[i].getClass().equals(Integer.class)) {
				editor.putInt(key[i], (Integer) value[i]);
			} else if (value[i].getClass().equals(Boolean.class)) {
				editor.putBoolean(key[i], (Boolean) value[i]);
			} else if (value[i].getClass().equals(String.class)) {
				editor.putString(key[i], (String) value[i]);
			} else if (value[i].getClass().equals(Float.class)) {
				editor.putFloat(key[i], (Integer) value[i]);
			} else {
				editor.putLong(key[i], (Long) value[i]);
			}
		}
		editor.commit();
	}

	public static void setParam(String filename, String key, Object value) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(filename, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		if (value.getClass().equals(Integer.class)) {
			editor.putInt(key, (Integer) value);
		} else if (value.getClass().equals(Boolean.class)) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value.getClass().equals(String.class)) {
			editor.putString(key, (String) value);
		} else if (value.getClass().equals(Float.class)) {
			editor.putFloat(key, (Integer) value);
		} else {
			editor.putLong(key, (Long) value);
		}
		editor.commit();
	}

	public static Object getParam(String filename, String key, Class<?> cls) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(filename, Context.MODE_PRIVATE);
		Object t = null;
		if (cls.equals(Integer.class)) {
			t = preferences.getInt(key, 0);
		} else if (cls.equals(String.class)) {
			t = preferences.getString(key, "");
		} else if (cls.equals(Float.class)) {
			t = preferences.getFloat(key, 0.0f);
		} else if (cls.equals(Long.class)) {
			t = preferences.getLong(key, 0);
		} else {
			t = preferences.getBoolean(key, false);
		}

		return t;

	}

	public static Object getParam(String filename, String key, Class<?> cls,boolean default_value) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(filename, Context.MODE_PRIVATE);
		Object t = null;
			t = preferences.getBoolean(key, default_value);

		return t;

	}

	public static SharedPreferences getInstance(String filenname) {

		return App.getInstance().getSharedPreferences(filenname, Context.MODE_PRIVATE);
	}

}
