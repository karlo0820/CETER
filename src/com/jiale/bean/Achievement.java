package com.jiale.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ѧ�ڳɼ���
 * @author 123
 *
 */
public class Achievement implements Serializable{
	private String semester;//ѧ��
	private List<Course> course;//��ѧ�ڿγ̳ɼ�
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
