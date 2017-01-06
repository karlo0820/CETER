package com.jiale.ceter;

import com.jiale.bean.Syllabus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class SyllabusInfo extends Activity {
	private ImageButton back;
	private TextView courseName;
	private TextView teacher;
	private TextView address;
	private TextView weeks;
	private TextView days;
	private TextView jie;
	Syllabus syllabus = new Syllabus();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tools_of_xskb_info);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getBundleExtra("info");
			syllabus = (Syllabus) bundle.getSerializable("syllabusInfo");
		}
		initView();
		super.onCreate(savedInstanceState);

	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.tool_item_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		courseName = (TextView) findViewById(R.id.xskb_info_courseName_text);
		teacher = (TextView) findViewById(R.id.xskb_info_tearcher_text);
		address = (TextView) findViewById(R.id.xskb_info_address_text);
		weeks = (TextView) findViewById(R.id.xskb_info_weeks_text);
		days = (TextView) findViewById(R.id.xskb_info_days_text);
		jie = (TextView) findViewById(R.id.xskb_info_jie_text);
		if (syllabus != null) {
			courseName.setText(syllabus.getCourseName());
			teacher.setText(syllabus.getTearcher());
			address.setText(syllabus.getAddress());
			days.setText("星期"+syllabus.getDay());
			jie.setText("第" + syllabus.getJie() + "大节");
			String string = "";
			if (syllabus.getEndUP() == 0) {// 单周
				string = "第" + syllabus.getStartUP() + "周";
			} else if (syllabus.getStartDown() == 0) {// 只有上段周数
				string = "第" + syllabus.getStartUP() + "-" + syllabus.getEndUP() + "周";
			} else {
				string = "第" + syllabus.getStartUP() + "-" + syllabus.getEndUP() + "," + syllabus.getStartDown() + "-"
						+ syllabus.getEndDown() + "周";
			}
			weeks.setText(string);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		resultCode = 0;
		data.putExtra("info", syllabus);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void finish() {
		overridePendingTransition(0, R.anim.slide_left_out);
		super.finish();
	}
}
