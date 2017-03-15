package com.peng.saishi.entity;

import java.io.File;
import java.io.Serializable;

public class MyFIleInfo implements Serializable {

	private String name;// 名字
	private String size;// 大小
	private String create_time;// 创建时间
	private String end;// 后最名

	public MyFIleInfo(String name, String size, String create_time, String end,
			String type, String mimetype, String parent) {
		super();
		this.name = name;
		this.size = size;
		this.create_time = create_time;
		this.end = end;
		this.type = type;
		this.mimetype = mimetype;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	private String type;// 类别
	private String mimetype;// 类型
	private String parent;// 父目录

	public MyFIleInfo() {
		// TODO Auto-generated constructor stub
	}

}
