package com.jiale.adapter;

import java.util.List;

import com.jiale.bean.ListViewItem;
import com.jiale.ceter.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopupwindowAdapter extends BaseAdapter {
	private List<String> data;
	private LayoutInflater MyInflater;

	public PopupwindowAdapter(Context context, List<String> arr) {
		this.data = arr;
		this.MyInflater = LayoutInflater.from(context);
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
		ListViewItem item = null;
		if (convertView == null) {
			item = new ListViewItem();
			convertView = MyInflater.inflate(R.layout.popupwindow_listview_item, null);
			item.setTitle((TextView) convertView.findViewById(R.id.popupwindow_item_title));
			convertView.setTag(item);
		} else {
			item = (ListViewItem) convertView.getTag();
		}
		item.getTitle().setText(data.get(position));
		return convertView;
	}

}
