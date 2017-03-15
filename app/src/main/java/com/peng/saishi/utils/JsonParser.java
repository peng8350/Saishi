package com.peng.saishi.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 解析ＪＳＯＮ的工具类
 * 
 * @author peng 1. 带双重list的形式 2.带list的形式 3.直接以ＪＡＶＡＢＥＡＮ的形式返回
 */
public class JsonParser {

	// 解析单个
	public static <T> T getBeanFromJson(JSONObject object, Class<T> cls) {
		T t = null;
		try {
			t = cls.newInstance();
			Iterator<String> keys = object.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				Object value = object.get(key);

				Field field = cls.getDeclaredField(key);
				field.setAccessible(true);
				if (value.getClass().equals(Integer.class)) {
					field.setInt(t, Integer.parseInt(value.toString()));
				} else if(value.getClass().equals(String.class)) {
					field.set(t, value.toString());
				}
				else if(value.getClass().equals(Double.class)){
					field.set(t, Double.parseDouble(value.toString()));
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return t;
	}
	
	//解析一堆字符串list
	public static List<String> getStringsFromJson(JSONArray array){
		List<String> params = new ArrayList<>();
		try {
			for (int i = 0; i < array.length(); i++) {
					
				params.add(array.getString(i));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODOlis Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

	// 解析一重list
	public static <T> List<T> getListFromJson(JSONArray array, Class<T> cls) {

		List<T> list = new ArrayList<T>();
		try {
			for (int i = 0; i < array.length(); i++) {
				T t = null;
				JSONObject object = array.getJSONObject(i);
				t = cls.newInstance();
				Iterator<String> keys = object.keys();
				while (keys.hasNext()) {
					
					String key = keys.next();
					Object value = object.get(key);

					Field field = cls.getDeclaredField(key);
					field.setAccessible(true);
					if (value.getClass().equals(Integer.class)) {
						field.setInt(t, Integer.parseInt(value.toString()));
					} else  if(value.getClass().equals(String.class)){

						field.set(t, value.toString());
					}
					else if(value.getClass().equals(Double.class)){
						field.set(t, Double.parseDouble(value.toString()));
					}
					else{
						field.set(t, Long.parseLong(value.toString()));
					}
				}
				list.add(t);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODOlis Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	// 解析二重list
	public static <T> List<List<T>> getListListFromJson(JSONArray array, Class<T> cls) {
		List<List<T>> lists = new ArrayList<List<T>>();
		try {
			for (int j = 0; j < array.length(); j++) {
				JSONArray array1=array.getJSONArray(j);
				List<T> list=new ArrayList<T>();
				for (int i = 0; i < array1.length(); i++) {
					T t = null;
					JSONObject object = array1.getJSONObject(i);
					t = cls.newInstance();
					Iterator<String> keys = object.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						Object value = object.get(key);

						Field field = cls.getDeclaredField(key);
						field.setAccessible(true);
						if (value.getClass().equals(Integer.class)) {
							field.setInt(t, Integer.parseInt(value.toString()));
						} else  if(value.getClass().equals(String.class)) {

							field.set(t, value.toString());
						}
					}
					list.add(t);
				}
				lists.add(list);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODOlis Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lists;
	}
}
