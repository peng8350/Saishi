package com.peng.saishi.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Pingjia implements Parcelable{
	private int id;
	private String name;
	private String picUrl;
	private String good;
	private String bad;
	private String content;
	private String time;

	public Pingjia(Parcel source) {
		// TODO Auto-generated constructor stub
		id = source.readInt();
		name = source.readString();
		picUrl = source.readString();
		good = source.readString();
		bad =source.readString();
		content = source.readString();
		time = source.readString();
	}
	
	public Pingjia() {
		// TODO Auto-generated constructor stub
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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}

	public String getBad() {
		return bad;
	}

	public void setBad(String bad) {
		this.bad = bad;
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





	public static final Creator<Pingjia> CREATOR = new Creator<Pingjia>() {

		@Override
		public Pingjia createFromParcel(Parcel source) {
			// TODO Auto-generated method stub

			return new Pingjia(source);
		}

		@Override
		public Pingjia[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Pingjia[size];
		}
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(picUrl);
		dest.writeString(good);
		dest.writeString(bad);
		dest.writeString(content);
		dest.writeString(time);
	}

}
