package com.jiale.bean;

import android.widget.ImageView;
import android.widget.TextView;

public class RefreshViewItem {
	private ImageView newImg;
	private TextView newTitle;//���ű���
	private TextView newTime;//���ŷ���ʱ��
	private TextView newAuthor;//���ŷ�����

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
