package com.jiale.bean;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListViewItem {
	private ImageView mimeType;
	private TextView fileName;
	private TextView fileSize;
	private TextView createTime;
	private CheckBox  checkBox;
	public ImageView getMimeType() {
		return mimeType;
	}

	public void setMimeType(ImageView mimeType) {
		this.mimeType = mimeType;
	}

	public TextView getFileName() {
		return fileName;
	}

	public void setFileName(TextView fileName) {
		this.fileName = fileName;
	}

	public TextView getFileSize() {
		return fileSize;
	}

	public void setFileSize(TextView fileSize) {
		this.fileSize = fileSize;
	}

	public TextView getCreateTime() {
		return createTime;
	}

	public void setCreateTime(TextView createTime) {
		this.createTime = createTime;
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
}
