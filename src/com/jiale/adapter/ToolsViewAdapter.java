package com.jiale.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiale.bean.ListViewItem;
import com.jiale.ceter.R;

public class ToolsViewAdapter extends BaseAdapter {
	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;

	public ToolsViewAdapter(Context context, List<Map<String, Object>> data) {
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
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
			convertView = layoutInflater.inflate(R.layout.tools_listview_item, null);
			item.setTitle((TextView) convertView.findViewById(R.id.tool_title));
			convertView.setTag(item);
		} else {
			item = (ListViewItem) convertView.getTag();
		}
		item.getTitle().setText((String) data.get(position).get("title"));
		return convertView;
	}

}
