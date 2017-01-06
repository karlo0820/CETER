package com.jiale.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 学期成绩类
 * @author 123
 *
 */
public class Achievement implements Serializable{
	private String semester;//学期
	private List<Course> course;//该学期课程成绩
	public Achievement() {
		super();
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public List<Course> getCourse() {
		return course;
	}
	public void setCourse(List<Course> course) {
		this.course = course;
	}
	
	
}
