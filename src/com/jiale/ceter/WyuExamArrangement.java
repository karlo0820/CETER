package com.jiale.ceter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class WyuExamArrangement extends FragmentActivity {
	private ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.wyu_exam);
		super.onCreate(savedInstanceState);
		initView();
		initEvent();
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.tool_item_back);
		WyuJWFragment cnf = new WyuJWFragment();
		Bundle bundle = new Bundle();
		bundle.putStringArray("wyuExam", new String[] { "http://jwc.wyu.edu.cn/www/newslist.asp?id=52",
				"http://jwc.wyu.edu.cn/www/newslist.asp?id=52&skey=&pg=" });
		cnf.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.wyu_exam_layout, cnf).commit();
	}

	private void initEvent() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
