package com.peng.saishi.interfaces.service;


/***
 * 网络请求的工具累
 * 
 * @author peng
 *
 */
public interface HttpService {

	public void connect(String Url);

	public void connect(String Url, String[] keys, String[] values);
	
}

