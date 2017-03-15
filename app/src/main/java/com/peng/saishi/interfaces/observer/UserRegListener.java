package com.peng.saishi.interfaces.observer;

import org.json.JSONObject;

public interface UserRegListener{

	public void onSuccess(JSONObject obj);
	
	public void onFailed(String msg);
}
