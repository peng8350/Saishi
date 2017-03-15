package com.peng.saishi.entity;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MatchInfo extends SugarRecord implements Serializable {

	public List<NewInfo> getNews() {
		return news;
	}

	public void setNews(List<NewInfo> news) {
		this.news = news;
	}
	
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return Long.parseLong(id+"");
	}
	
	@Column(name="mid",unique=true)
	private int id;
	
	private String name;
	private double score;
	private int type1;
	private int type2;
	private String time;
	private String place;
	private String zhuban;
	private int visitPerson;
	private String fenlei;
	private String webUrl;
	private String content;
	private String titles;
	@Ignore
	private List<NewInfo> news;
	@Ignore
	private List<QuestionInfo> questions;

	public List<QuestionInfo> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionInfo> questions) {
		this.questions = questions;
	}

	public MatchInfo(String titles) {
		super();
		this.titles = titles;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	@Ignore
	private List<Pingjia> pingjias;

	public int getType1() {
		return type1;
	}

	public void setType1(int type1) {
		this.type1 = type1;
	}

	public int getType2() {
		return type2;
	}

	public void setType2(int type2) {
		this.type2 = type2;
	}

	public List<Pingjia> getPingjias() {
		return pingjias;
	}

	public void setPingjias(List<Pingjia> pingjias) {
		this.pingjias = pingjias;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return score;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getZhuban() {
		return zhuban;
	}

	public void setZhuban(String zhuban) {
		this.zhuban = zhuban;
	}

	public int getVisitPerson() {
		return visitPerson;
	}

	public void setVisitPerson(int visitPerson) {
		this.visitPerson = visitPerson;
	}

	public String getFenlei() {
		return fenlei;
	}

	public void setFenlei(String fenlei) {
		this.fenlei = fenlei;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MatchInfo() {
		// TODO Auto-generated constructor stub
	}

}
