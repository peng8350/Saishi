package com.peng.saishi.entity;

import com.orm.SugarRecord;
public class NoticeInfo extends SugarRecord {
	private int userid;
	public int getUserid() {
		return userid;
	}

	public NoticeInfo(int userid, String time, String name, String content,
			int targetid) {
		super();
		this.userid = userid;
		this.time = time;
		this.name = name;
		this.content = content;
		this.targetid = targetid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}


	private String time;
	private String name;
	private String content;
	private int targetid;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public int getTargetid() {
		return targetid;
	}

	public void setTargetid(int targetid) {
		this.targetid = targetid;
	}



	public NoticeInfo() {
		// TODO Auto-generated constructor stub
	}

}
