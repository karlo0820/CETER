package com.jiale.bean;

import java.io.Serializable;

/**
 * 课程信息类
 * 
 * @author 123
 *
 */
public class Syllabus implements Serializable{
	private String courseName; // 课程名
	private String tearcher; // 任课老师
	private String address;// 上课地点
	private int startUP; // 课程上段开始周数
	private int endUP;// 课程上段结束周数
	private int startDown;// 课程下段开始周数
	private int endDown;// 课程下段结束周数
	private int day;// 星期几
	private int jie;// 第几大节

	public Syllabus() {
		super();
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTearcher() {
		return tearcher;
	}

	public void setTearcher(String tearcher) {
		this.tearcher = tearcher;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStartUP() {
		return startUP;
	}

	public void setStartUP(int startUP) {
		this.startUP = startUP;
	}

	public int getEndUP() {
		return endUP;
	}

	public void setEndUP(int endUP) {
		this.endUP = endUP;
	}

	public int getStartDown() {
		return startDown;
	}

	public void setStartDown(int startDown) {
		this.startDown = startDown;
	}

	public int getEndDown() {
		return endDown;
	}

	public void setEndDown(int endDown) {
		this.endDown = endDown;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getJie() {
		return jie;
	}

	public void setJie(int jie) {
		this.jie = jie;
	}

	@Override
	public String toString() {
		return "Syllabus [courseName=" + courseName + ", tearcher=" + tearcher + ", address=" + address + ", startUP="
				+ startUP + ", endUP=" + endUP + ", startDown=" + startDown + ", endDown=" + endDown + ", day=" + day
				+ ", jie=" + jie + "]";
	}


}