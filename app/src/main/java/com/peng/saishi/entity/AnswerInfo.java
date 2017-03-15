package com.peng.saishi.entity;

public class AnswerInfo {
	private int id;
	private String username;
	private String content;
	private String time;
	private int userid;

	private int questionid;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public AnswerInfo(int id, String username, String content, String time,
			int userid, int questionid) {
		super();
		this.id = id;
		this.username = username;
		this.content = content;
		this.time = time;
		this.userid = userid;
		this.questionid = questionid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public int getQuestionid() {
		return questionid;
	}

	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}

	public AnswerInfo() {
		// TODO Auto-generated constructor stub
	}

}
