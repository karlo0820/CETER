package com.jiale.bean;

import java.io.Serializable;

/**
 * �γ̳ɼ���
 * 
 * @author 123
 *
 */
public class Course implements Serializable {
	private String courseNumber; // �γ̺�
	private String courseName;// �γ���
	private String category;// ����
	private String credit;// ѧ��
	private String score;// �ɼ�
	private String remark;// ��ע

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
