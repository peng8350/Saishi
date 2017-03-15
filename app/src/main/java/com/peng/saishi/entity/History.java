package com.peng.saishi.entity;

public class History {
	private int id;
	private String name;
	private String content;
	private String time;
	private int team_pic;
	public int getTeam_pic() {
		return team_pic;
	}
	public void setTeam_pic(int team_pic) {
		this.team_pic = team_pic;
	}
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
	public History(int id, String name, String content, String time) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
		this.time = time;
	}
	public History() {
		// TODO Auto-generated constructor stub
	}
}
