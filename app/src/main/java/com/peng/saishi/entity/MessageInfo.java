package com.peng.saishi.entity;

import cn.jpush.im.android.api.model.Conversation;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

@Table(name = "Message")
public class MessageInfo {
	@Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
	private int id;
	@Transient
	private int teamid;
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	private int userid;
	private String name;
	private Long time;
	private String content;
	@Transient
	private Conversation conver;
	public Conversation getConver() {
		return conver;
	}

	public void setConver(Conversation conver) {
		this.conver = conver;
	}

	private int weidu;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public MessageInfo(int teamid, int userid, String name, long time, String content, 
			int weidu, int type) {
		super();
		this.teamid = teamid;
		this.userid = userid;
		this.name = name;
		this.time = time;
		this.content = content;
		this.weidu = weidu;
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getWeidu() {
		return weidu;
	}

	public void setWeidu(int weidu) {
		this.weidu = weidu;
	}



	public int getTeamid() {
		return teamid;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}

	public MessageInfo() {
		// TODO Auto-generated constructor stub
	}

}
