package com.jiale.bean;

import android.widget.ImageView;
import android.widget.TextView;

public class RefreshViewItem {
	private ImageView newImg;
	private TextView newTitle;//新闻标题
	private TextView newTime;//新闻发布时间
	private TextView newAuthor;//新闻发布者

	public ImageView getNewImg() {
		return newImg;
	}

	public void setNewImg(ImageView newImg) {
		this.newImg = newImg;
	}

	public TextView getNewTitle() {
		return newTitle;
	}

	public void setNewTitle(TextView newTitle) {
		this.newTitle = newTitle;
	}

	public TextView getNewTime() {
		return newTime;
	}

	public void setNewTime(TextView newTime) {
		this.newTime = newTime;
	}

	public TextView getNewAuthor() {
		return newAuthor;
	}

	public void setNewAuthor(TextView newAuthor) {
		this.newAuthor = newAuthor;
	}
}
