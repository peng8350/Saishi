package com.peng.saishi.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class JoinGroup implements Parcelable {
	private int id;
	private int uid;// 申请人
	private int pid;// 群主
	private int status;// 状态
	private String time;
	private String content;
	private int groupid;
	private String teamname;
	private int picid;
	private String target;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getPicid() {
		return picid;
	}

	public void setPicid(int picid) {
		this.picid = picid;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public JoinGroup(int id, int uid, int pid, int status, String content) {
		super();
		this.id = id;
		this.uid = uid;
		this.pid = pid;
		this.status = status;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public JoinGroup() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static Creator<JoinGroup> CREATOR = new Creator<JoinGroup>() {

		@Override
		public JoinGroup createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			// private int uid;// 申请人
			// private int pid;// 群主
			// private int status;// 状态
			// private String time;
			// private String content;
			JoinGroup group = new JoinGroup();
			group.uid = source.readInt();
			group.pid = source.readInt();
			group.status = source.readInt();
			group.time = source.readString();
			group.content = source.readString();
			return group;
		}

		@Override
		public JoinGroup[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JoinGroup[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(uid);
		dest.writeInt(pid);
		dest.writeInt(status);
		dest.writeString(time);
		dest.writeString(content);
	}

}
