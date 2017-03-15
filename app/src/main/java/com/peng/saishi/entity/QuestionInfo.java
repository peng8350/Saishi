package com.peng.saishi.entity;

import java.io.Serializable;

public class QuestionInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7869484835020460673L;
	private int id;
	private String title;
	private int visit;
	private String time;
	private int matchid;
	private int userid;
	private String username;
	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	private String content;
	private boolean show;
	private boolean checked;
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private String matchName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}



	public QuestionInfo(int id, String title, int visit, String time,
			int matchid, int userid, String username, String content,
			String matchName) {
		super();
		this.id = id;
		this.title = title;
		this.visit = visit;
		this.time = time;
		this.matchid = matchid;
		this.userid = userid;
		this.username = username;
		this.content = content;
		this.matchName = matchName;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public int getVisit() {
		return visit;
	}

	public void setVisit(int visit) {
		this.visit = visit;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


	public int getMatchid() {
		return matchid;
	}

	public void setMatchid(int matchid) {
		this.matchid = matchid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public QuestionInfo() {
		// TODO Auto-generated constructor stub
	}

}
