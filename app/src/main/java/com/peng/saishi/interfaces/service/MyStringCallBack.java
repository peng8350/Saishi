package com.peng.saishi.interfaces.service;

import okhttp3.Call;

import com.zhy.http.okhttp.callback.StringCallback;

public abstract class MyStringCallBack extends StringCallback {

	@Override
	public void onError(Call arg0, Exception arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println("捕获到错误okhttputils,错误id"+arg2);
	}

	@Override
	public void onResponse(String arg0, int arg1) {
		// TODO Auto-generated method stub
			System.out.println("请求成功");
	}
	
	

}
