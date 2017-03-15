package com.peng.saishi.entity;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

@SuppressWarnings("serial")
@Table(name = "News")
public class NewInfo extends SugarRecord implements Serializable{
	private long id;
	private long matchid;
	private String title;
	private String content;
	private String webUrl;
	private String time;

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public long getMatchid() {
		return matchid;
	}

	public void setMatchid(long matchid) {
		this.matchid = matchid;
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

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public NewInfo() {
		// TODO Auto-generated constructor stub
	}
	


	public NewInfo(int id, int matchid, String title, String content,
			String webUrl, String time) {
		super();
		this.id = id;
		this.matchid = matchid;
		this.title = title;
		this.content = content;
		this.webUrl = webUrl;
		this.time = time;
	}

}
