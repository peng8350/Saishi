package com.peng.saishi.interfaces.observer;

public interface NetworkListener {
	
	//网络断开
	public void onDisConnect();
	
	//网络hilum
	public void onConnect();

}
