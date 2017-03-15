package com.peng.saishi.entity;

import cn.jpush.im.android.api.model.Message;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "Chat")
public class ChatEntity {
	@Id
	// 如果主键没有命名名为id或_id的时，需要为主键添加此注解
	private int id;
	private int teamid;
	private int userid;
	private String content;
	private String time;
	private int type;



	public ChatEntity(int teamid, int userid, String content, String time,
			int type, String name) {
		super();
		this.teamid = teamid;
		this.userid = userid;
		this.content = content;
		this.time = time;
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public ChatEntity() {
		// TODO Auto-generated constructor stub
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTeamid() {
		return teamid;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

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

	private String name;

}