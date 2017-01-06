package com.jiale.bean;

import java.io.Serializable;

public class CET implements Serializable {
	private String level;// 等级
	private String hearing;// 听力
	private String reading;// 阅读
	private String writing;// 写作
	private String student;// 考生姓名
	private String school;// 学校
	private String totalpoints;// 总分
	private String cetid;// 准考证

	public CET() {
		super();
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getHearing() {
		return hearing;
	}

	public void setHearing(String hearing) {
		this.hearing = hearing;
	}

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
	}

	public String getWriting() {
		return writing;
	}

	public void setWriting(String writing) {
		this.writing = writing;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getTotalpoints() {
		return totalpoints;
	}

	public void setTotalpoints(String totalpoints) {
		this.totalpoints = totalpoints;
	}

	public String getCetid() {
		return cetid;
	}

	public void setCetid(String cetid) {
		this.cetid = cetid;
	}

}
