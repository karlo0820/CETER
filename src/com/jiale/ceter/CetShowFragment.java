package com.jiale.ceter;

import com.jiale.bean.CET;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CetShowFragment extends Fragment {
	private TextView student;
	private TextView level;
	private TextView school;
	private TextView cetid;
	private TextView totalpoints;
	private TextView hearing;
	private TextView reading;
	private TextView writing;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tools_of_cet_show, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		student = (TextView) view.findViewById(R.id.cet_show_student);
		level = (TextView) view.findViewById(R.id.cet_show_level);
		school = (TextView) view.findViewById(R.id.cet_show_school);
		cetid = (TextView) view.findViewById(R.id.cet_show_id);
		totalpoints = (TextView) view.findViewById(R.id.cet_show_totalpoints);
		hearing = (TextView) view.findViewById(R.id.cet_show_hearing);
		reading = (TextView) view.findViewById(R.id.cet_show_reading);
		writing = (TextView) view.findViewById(R.id.cet_show_writing);
		Bundle bundle = getArguments();
		CET cet = (CET) bundle.getSerializable("cet");

		student.setText(cet.getStudent());
		level.setText(cet.getLevel());
		school.setText(cet.getSchool());
		cetid.setText(cet.getCetid());
		totalpoints.setText(cet.getTotalpoints());
		hearing.setText(cet.getHearing());
		reading.setText(cet.getReading());
		writing.setText(cet.getWriting());
	}

}
