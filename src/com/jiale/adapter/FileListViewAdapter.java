package com.jiale.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiale.bean.FileInfo;
import com.jiale.bean.FileListViewItem;
import com.jiale.ceter.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListViewAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<FileInfo> data;
	public List<Boolean> checkList;
	private CheckBox checkBox;
	private HideListener listenser;

	public FileListViewAdapter(Context context, List<FileInfo> data) {
		layoutInflater = LayoutInflater.from(context);
		this.data = data;
		Log.i("DATA", data.size() + "");
		initData();
	}

	public List<Boolean> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<Boolean> checkList) {
		this.checkList = checkList;
	}

	public HideListener getListenser() {
		return listenser;
	}

	public void setListenser(HideListener listenser) {
		this.listenser = listenser;
	}

	private void initData() {
		checkList = new ArrayList<Boolean>();
		for (int i = 0; i < data.size(); i++) {
			checkList.add(false);
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FileListViewItem item = null;
		if (convertView == null) {
			item = new FileListViewItem();
			convertView = layoutInflater.inflate(R.layout.download_listview_item, null);
			ImageView mimeType = (ImageView) convertView.findViewById(R.id.download_listview_item_mimetype);
			TextView fileName = (TextView) convertView.findViewById(R.id.download_listview_item_filename);
			TextView fileSize = (TextView) convertView.findViewById(R.id.download_listview_item_filesize);
			TextView createTime = (TextView) convertView.findViewById(R.id.download_listview_item_createtime);
			checkBox = (CheckBox) convertView.findViewById(R.id.download_listview_item_checkbox);
			item.setMimeType(mimeType);
			item.setFileName(fileName);
			item.setCreateTime(createTime);
			item.setFileSize(fileSize);
			item.setCheckBox(checkBox);
			convertView.setTag(item);
		} else {
			item = (FileListViewItem) convertView.getTag();
		}
		FileInfo file = data.get(position);
		Log.i("File", file.getName());
		item.getCheckBox().setChecked(checkList.get(position));
		item.getFileName().setText(file.getName());
		item.getFileSize().setText(file.getSize());
		item.getCreateTime().setText(file.getCreateTime());
		final CheckBox check = item.getCheckBox();
		check.setChecked(checkList.get(position));
		final int index = position;
		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkList.get(index)) {
					checkList.set(index, false);
					Log.i("CheckBox", "false");
				} else {
					checkList.set(index, true);
					Log.i("CheckBox", "true");
				}
				int num = getCheckNum();
				if (num > 0) {
					if (listenser != null) {
						Log.i("hidelayout", "show");
						listenser.show();
					}
				} else {
					if (listenser != null) {
						Log.i("hidelayout", "hide");
						listenser.hide();
					}
				}
				if (num == 1) {
					if (listenser != null) {
						Log.i("OPNE", "TRUE");
						listenser.isClick(true);
					}
				} else {
					if (listenser != null) {
						Log.i("OPNE", "false");
						listenser.isClick(false);
					}
				}
				if (listenser != null) {
					Log.i("FileNum", "" + num);
					listenser.changeCheckNum(num);
				}
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	public int getCheckNum() {
		int num = 0;
		for (int i = 0; i < checkList.size(); i++) {
			if (checkList.get(i)) {
				num++;
			}
		}
		return num;
	}

	public interface HideListener {
		public void hide();

		public void show();

		public void isClick(boolean click);

		public void changeCheckNum(int num);
	}
}
