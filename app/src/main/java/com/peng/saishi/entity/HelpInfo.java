package com.peng.saishi.entity;

import java.io.Serializable;
import java.util.List;

public class HelpInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8014404574164820073L;
	public HelpInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public HelpInfo(String title, List<List<String>> contents) {
		super();
		this.title = title;
		this.contents = contents;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<List<String>> getContents() {
		return contents;
	}

	public void setContents(List<List<String>> contents) {
		this.contents = contents;
	}

	private String title;
	private List<List<String>> contents;

}
