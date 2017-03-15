package com.peng.saishi.entity;

public class ChatRecord {
	private int id;
	private String title, content, head, time;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ChatRecord(int id, String title, String content, String head,
			String time) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.head = head;
		this.time = time;
	}

	public ChatRecord() {
		// TODO Auto-generated constructor stub
	}
}
