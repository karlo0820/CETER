package com.jiale.bean;

import java.io.Serializable;

/**
 * �γ���Ϣ��
 * 
 * @author 123
 *
 */
public class Syllabus implements Serializable{
	private String courseName; // �γ���
	private String tearcher; // �ο���ʦ
	private String address;// �Ͽεص�
	private int startUP; // �γ��϶ο�ʼ����
	private int endUP;// �γ��϶ν�������
	private int startDown;// �γ��¶ο�ʼ����
	private int endDown;// �γ��¶ν�������
	private int day;// ���ڼ�
	private int jie;// �ڼ����

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