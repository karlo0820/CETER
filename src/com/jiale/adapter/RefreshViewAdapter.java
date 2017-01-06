package com.jiale.adapter;

import java.util.List;

import com.jiale.bean.RefreshViewItem;
import com.jiale.bean.WyuNew;
import com.jiale.ceter.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RefreshViewAdapter extends BaseAdapter {
	private List<WyuNew> data;
	private LayoutInflater layoutInflater;

	public RefreshViewAdapter(Context context, List<WyuNew> data) {
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
		RefreshViewItem item = null;
		if (convertView == null) {
			item = new RefreshViewItem();
			convertView = layoutInflater.inflate(R.layout.refreshlistview_item, null);
			item.setNewAuthor((TextView) convertView.findViewById(R.id.new_author));
			item.setNewTime((TextView) convertView.findViewById(R.id.new_time));
			item.setNewTitle((TextView) convertView.findViewById(R.id.new_title));
			convertView.setTag(item);
		} else {
			item = (RefreshViewItem) convertView.getTag();
		}

		item.getNewAuthor().setText(data.get(position).getPublicAuthor());
		item.getNewTime().setText(data.get(position).getPublicTime());
		item.getNewTitle().setText(data.get(position).getTitle());
		return convertView;
	}

}
