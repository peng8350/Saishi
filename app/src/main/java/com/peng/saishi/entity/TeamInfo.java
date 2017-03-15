package com.peng.saishi.entity;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamInfo implements Parcelable{
	private int id;
	private int matchid;
	private String name;
	private String owner;
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	private long groupid;

	public long getGroupid() {
		return groupid;
	}

	public void setGroupid(long groupid) {
		this.groupid = groupid;
	}

	private String matchName;
	public int getMatchid() {
		return matchid;
	}

	public void setMatchid(int matchid) {
		this.matchid = matchid;
	}

	private int nowperson;
	private int needperson;
	private String time;
	private String intro;
	private String yaoqiu;
	private String plan;
	private String target;
	private String type;
	private String tags;
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	private List<User> members;

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

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public int getNowperson() {
		return nowperson;
	}

	public void setNowperson(int nowperson) {
		this.nowperson = nowperson;
	}

	public int getNeedperson() {
		return needperson;
	}

	public void setNeedperson(int needperson) {
		this.needperson = needperson;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getYaoqiu() {
		return yaoqiu;
	}

	public void setYaoqiu(String yaoqiu) {
		this.yaoqiu = yaoqiu;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public TeamInfo(int id, String name, String matchName, int nowperson, int needperson, String time, 
			String intro, String yaoqiu, String plan, String target, String type,String tags) {
		super();
		this.id = id;
		this.name = name;
		this.matchName = matchName;
		this.nowperson = nowperson;
		this.needperson = needperson;
		this.time = time;
		this.intro = intro;
		this.yaoqiu = yaoqiu;
		this.plan = plan;
		this.target = target;
		this.type = type;
		this.tags= tags;
	}

	public TeamInfo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeInt(matchid);
		dest.writeString(name);
		dest.writeString(matchName);
		dest.writeInt(nowperson);
		dest.writeInt(needperson);
		dest.writeString(time);
		dest.writeString(intro);
		dest.writeString(yaoqiu);
		dest.writeString(plan);
		dest.writeString(target);
		dest.writeString(type);
		dest.writeString(tags);
		dest.writeList(members);
		dest.writeLong(groupid);
		dest.writeString(owner);
	}
	
	public static Creator<TeamInfo> CREATOR = new Creator<TeamInfo>() {

		@SuppressWarnings("unchecked")
		@Override
		public TeamInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TeamInfo info = new TeamInfo();
			info.id = source.readInt();
			info.matchid = source.readInt();
			info.name = source.readString();
			info.matchName = source.readString();
			info.nowperson = source.readInt();
			info.needperson = source.readInt();
			info.time =source.readString();
			info.intro =  source.readString();
			info.yaoqiu = source.readString();
			info.plan = source.readString();
			info.target = source.readString();
			info.type = source.readString();
			info.tags = source.readString();
			info.members = source.readArrayList(User.class.getClassLoader());
			info.groupid = source.readLong();
			info.owner = source.readString();
			return info;
		}

		@Override
		public TeamInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TeamInfo[size];
		}
	};
}
