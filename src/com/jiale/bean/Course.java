package com.jiale.bean;

import java.io.Serializable;

/**
 * 课程成绩类
 * 
 * @author 123
 *
 */
public class Course implements Serializable {
	private String courseNumber; // 课程号
	private String courseName;// 课程名
	private String category;// 类型
	private String credit;// 学分
	private String score;// 成绩
	private String remark;// 备注

	public Course() {
		super();
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
