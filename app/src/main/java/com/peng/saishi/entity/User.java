package com.peng.saishi.entity;

import com.lidroid.xutils.db.annotation.Transient;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;

import java.io.Serializable;
import java.util.List;

public class User extends SugarRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2842787180660624040L;
	@Column(name = "uid", unique = true)
	// int,long类型的id默认自增，不想使用自增时添加此注解
	private int id;
	private String user;
	private String pswd;
	private String username;
	@Transient
	private String intro;
	@Transient
	private String sex;
	@Transient
	private String school;
	public void setReal_address(String real_address) {
		this.real_address = real_address;
	}

	@Transient
	private String xueli;
	@Transient
	private int age;
	@Transient
	private String place;
	@Transient
	private String major;
	@Transient
	private String phone;
	@Transient
	private String hobby;
	@Transient
	private String channelid;
	@Transient
	private int unreadcount;
	@Ignore
	private String real_address;

	public String getReal_address() {
		return real_address;
	}

	public int getUnreadcount() {
		return unreadcount;
	}

	public void setUnreadcount(int unreadcount) {
		this.unreadcount = unreadcount;
	}

	@Ignore
	private List<MatchInfo> matchs;// 所有的比赛
	@Ignore
	private List<MatchInfo> shoucangs;// 所有的比赛

	public List<MatchInfo> getShoucangs() {
		return shoucangs;
	}

	public void setShoucangs(List<MatchInfo> shoucangs) {
		this.shoucangs = shoucangs;
	}

	public List<TeamInfo> getTeam1() {
		return team1;
	}

	public void setTeam1(List<TeamInfo> team1) {
		this.team1 = team1;
	}

	public List<TeamInfo> getTeam2() {
		return team2;
	}

	public void setTeam2(List<TeamInfo> team2) {
		this.team2 = team2;
	}

	@Ignore
	private List<TeamInfo> team1, team2;// 用户的队伍

	public List<MatchInfo> getMatchs() {
		return matchs;
	}

	public void setMatchs(List<MatchInfo> matchs) {
		this.matchs = matchs;
	}

	public Long getId() {
		return Long.parseLong(id + "");
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPswd() {
		return pswd;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getXueli() {
		return xueli;
	}

	public void setXueli(String xueli) {
		this.xueli = xueli;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public User(String user, String pswd, String username, String intro,
			String sex, String school, String xueli, int age, String place,
			String major, String phone, String hobby, String channelid) {
		super();
		this.user = user;
		this.pswd = pswd;
		this.username = username;
		this.intro = intro;
		this.sex = sex;
		this.school = school;
		this.xueli = xueli;
		this.age = age;
		this.place = place;
		this.major = major;
		this.phone = phone;
		this.hobby = hobby;
		this.channelid = channelid;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

}
