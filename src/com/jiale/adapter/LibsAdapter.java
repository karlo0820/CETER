package com.jiale.adapter;

import java.util.List;

import com.jiale.bean.Books;
import com.jiale.bean.LibsListViewItem;
import com.jiale.ceter.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LibsAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Books> data;
	private Books book;

	public LibsAdapter(Context context, List<Books> data) {
		layoutInflater = LayoutInflater.from(context);
		this.data = data;
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
		LibsListViewItem item = null;
		if (convertView == null) {
			item = new LibsListViewItem();
			convertView = layoutInflater.inflate(R.layout.tools_of_libs_listview_item, null);
			item.setTv_flat((TextView) convertView.findViewById(R.id.book_flat_text));
			item.setTv_last((TextView) convertView.findViewById(R.id.book_last_text));
			item.setTv_bookname((TextView) convertView.findViewById(R.id.book_bookname_text));
			item.setTv_lease((TextView) convertView.findViewById(R.id.book_lease_text));
			convertView.setTag(item);
		} else {
			item = (LibsListViewItem) convertView.getTag();
		}
		book = data.get(position);
		item.getTv_flat().setText(book.getFlat());
		item.getTv_last().setText(book.getLast());
		item.getTv_bookname().setText(book.getBookName());
		item.getTv_lease().setText(book.getLease());
		return convertView;
	}


}
