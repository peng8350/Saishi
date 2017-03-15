package com.peng.saishi.entity;

import java.io.File;

import com.lidroid.xutils.http.HttpHandler;

import cn.jpush.im.android.api.model.Message;

public class FileChatEntity extends ChatEntity {

	private double current;
	private Message msg;
	private boolean canceled;
	private HttpHandler<File> handler;

	public HttpHandler<File> getHandler() {
		return handler;
	}

	public void setHandler(HttpHandler<File> handler) {
		this.handler = handler;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}


	public FileChatEntity(int teamid, int userid, String content, String time,
			int type, String name, double current, Message msg) {
		super(teamid, userid, content, time, type, name);
		this.current = current;
		this.msg = msg;
	}

	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}



	public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}

	public FileChatEntity() {
		// TODO Auto-generated constructor stub
	}

}
