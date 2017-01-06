package com.jiale.ceter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class AboutApp extends Activity implements OnClickListener {
	private RelativeLayout help_layout;
	private RelativeLayout fundation_layout;
	private RelativeLayout checkVersion_layout;
	private ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.setting_about);
		super.onCreate(savedInstanceState);
		initView();
		initListener();
	}

	private void initView() {
		help_layout = (RelativeLayout) findViewById(R.id.setting_about_problem);
		fundation_layout = (RelativeLayout) findViewById(R.id.setting_about_fundation);
		checkVersion_layout = (RelativeLayout) findViewById(R.id.setting_about_checkversion);
		back = (ImageButton) findViewById(R.id.setting_about_back);
	}

	private void initListener() {
		help_layout.setOnClickListener(this);
		fundation_layout.setOnClickListener(this);
		checkVersion_layout.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_about_problem:
			
			break;
		case R.id.setting_about_fundation:
			Intent intent = new Intent(AboutApp.this, MyWebView.class);
			intent.putExtra("fundation", "file:///android_asset/fundation.html");
			startActivity(intent);
			break;
		case R.id.setting_about_checkversion:

			break;
		case R.id.setting_about_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
