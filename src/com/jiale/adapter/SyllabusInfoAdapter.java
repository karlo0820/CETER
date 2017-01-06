package com.jiale.adapter;

import java.util.List;
import java.util.Map;

import com.jiale.bean.Syllabus;
import com.jiale.ceter.R;
import com.jiale.view.MyGallery3D;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

public class SyllabusInfoAdapter extends BaseAdapter {
	private Context context;
	private TextView[] textViewList;

	public SyllabusInfoAdapter(Context context, List<Syllabus> list, Map<String, Integer> selecter) {
		this.context = context;
		textViewList = new TextView[list.size()];
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int length = wm.getDefaultDisplay().getWidth() / 2;
		if (selecter.size() > 0) {
			Log.i("selecter", "´æÔÚÑÕÉ«");
		}
		for (int i = 0; i < list.size(); i++) {
			Syllabus s = list.get(i);
			TextView tv = new TextView(context);
			tv.setText(s.getCourseName() + "@" + s.getAddress());
			if (selecter.containsKey(s.getCourseName())) {
				tv.setBackgroundResource(selecter.get(s.getCourseName()));
				Log.i("ÑÕÉ«", selecter.get(s.getCourseName()) + "");
			} else {
				tv.setBackgroundResource(R.drawable.kb_textview);
			}
			tv.setLayoutParams(new Gallery.LayoutParams(length, length));
			tv.setTextColor(Color.BLACK);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setPadding(10, 0, 0, 10);
			tv.setAlpha(0.7f);
			this.textViewList[i] = tv;
		}
	}

	@Override
	public int getCount() {
		return textViewList.length;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return textViewList[position];
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

}
