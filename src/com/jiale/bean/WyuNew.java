package com.jiale.bean;

import java.io.Serializable;

public class WyuNew implements Serializable {
	private String title;
	private String publicAuthor;
	private String href;
	private String publicTime;

	public WyuNew() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublicAuthor() {
		return publicAuthor;
	}

	public void setPublicAuthor(String publicAuthor) {
		this.publicAuthor = publicAuthor;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getPublicTime() {
		return publicTime;
	}

	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
	}
}
