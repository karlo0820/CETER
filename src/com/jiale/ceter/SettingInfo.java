package com.jiale.ceter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jiale.adapter.PopupwindowAdapter;
import com.jiale.bean.ServiceBind;
import com.jiale.bean.Syllabus;
import com.jiale.util.CacheUtil;
import com.jiale.util.ForCurrentWeekUtil;
import com.jiale.util.NetWorkStateService;
import com.jiale.util.SharedPreferencesHelper;
import com.jiale.util.SyllabusAlarm;
import com.jiale.view.MyPopupWindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingInfo extends Activity implements OnClickListener, OnCheckedChangeListener {
	private ToggleButton alarmBtn;

	private ToggleButton netBtn;
	private ImageButton back;
	private RelativeLayout syllabusAlarm;
	private RelativeLayout setWeek;
	private SharedPreferencesHelper preferenceHelper;
	private Dialog pickerDialog;
	private TextView currentweek;
	private TextView settime;
	private MyPopupWindow popwindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.setting_set);
		super.onCreate(savedInstanceState);
		initView();
		initEvent();
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.setting_set_back);
		alarmBtn = (ToggleButton) findViewById(R.id.setting_set_alarmBtn);
		netBtn = (ToggleButton) findViewById(R.id.setting_set_netAlarmBtn);
		syllabusAlarm = (RelativeLayout) findViewById(R.id.setting_set_alarm);
		setWeek = (RelativeLayout) findViewById(R.id.setting_set_currentweek);
		currentweek = (TextView) findViewById(R.id.setting_set_currentweek_week);
		preferenceHelper = new SharedPreferencesHelper(this, "setting");
		if (preferenceHelper.getBooleanPreferences("alarmState")) {
			alarmBtn.setChecked(true);
		} else {
			alarmBtn.setChecked(false);
		}
		if (preferenceHelper.getBooleanPreferences("netService")) {
			netBtn.setChecked(true);
		} else {
			netBtn.setChecked(false);
		}
		currentweek.setText("第" + ForCurrentWeekUtil.getCurrentWeek(preferenceHelper) + "周");

	}

	private void initEvent() {
		setWeek.setOnClickListener(this);
		back.setOnClickListener(this);
		alarmBtn.setOnCheckedChangeListener(this);
		netBtn.setOnCheckedChangeListener(this);
		syllabusAlarm.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.setting_set_alarmBtn:
			checkAlarm(isChecked);
			break;
		case R.id.setting_set_netAlarmBtn:
			netWorkNoti(isChecked);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_set_alarm:
			showTimePicker();
			break;
		case R.id.setting_set_back:
			finish();
			break;
		case R.id.setting_set_currentweek:
			showSelectWeek();
			break;
		default:
			break;
		}
	}

	private void netWorkNoti(boolean isChecked) {
		if (isChecked) {
			if (preferenceHelper.saveBooleanPreferences("netService", isChecked)) {
				MainActivity main = (MainActivity) ServiceBind.main;
				main.bind();
				// Intent intent = new Intent(this,
				// ReceiveNetWorkStateServicce.class);
				// startService(intent);
				Toast.makeText(SettingInfo.this, "网络状态服务提醒已经开启成功", Toast.LENGTH_SHORT).show();
			} else {
				netBtn.setChecked(false);
			}
		} else {
			if (preferenceHelper.saveBooleanPreferences("netService", isChecked)) {
				MainActivity main = (MainActivity) ServiceBind.main;
				main.unBind();
				// Intent intent = new Intent(this,
				// ReceiveNetWorkStateServicce.class);
				// stopService(intent);
				Toast.makeText(SettingInfo.this, "网络状态服务提醒已经关闭", Toast.LENGTH_SHORT).show();
			} else {
				netBtn.setChecked(true);
			}
		}
	}

	private void checkAlarm(boolean isChecked) {
		if (isChecked) {
			List<Syllabus> list = (List<Syllabus>) CacheUtil.getCache(SettingInfo.this, "syllabus");
			if (list != null) {
				if (preferenceHelper.saveBooleanPreferences("alarmState", isChecked)) {
					SyllabusAlarm.openAlarm(SettingInfo.this);
					Toast.makeText(SettingInfo.this, "课程通知已经开启成功", Toast.LENGTH_SHORT).show();
				} else {
					alarmBtn.setChecked(false);
				}
			} else {
				Toast.makeText(SettingInfo.this, "还未绑定课程，请前去绑定", Toast.LENGTH_SHORT).show();
				alarmBtn.setChecked(false);
			}
		} else {
			if (preferenceHelper.saveBooleanPreferences("alarmState", isChecked)) {
				SyllabusAlarm.cancelAlarm(SettingInfo.this);
				Toast.makeText(SettingInfo.this, "课程通知已经关闭", Toast.LENGTH_SHORT).show();
			} else {
				alarmBtn.setChecked(true);
			}
		}
	}

	private void showSelectWeek() {
		if (popwindow == null) {
			ListView listview = new ListView(this);
			List<String> arr = new ArrayList<String>();
			for (int i = 1; i <= 20; i++) {
				arr.add("第" + i + "周");
			}
			PopupwindowAdapter adapter = new PopupwindowAdapter(this, arr);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					if (preferenceHelper != null) {
						if (preferenceHelper.saveLongPreferences("weekMillsSecond", System.currentTimeMillis())
								&& preferenceHelper.saveIntPreferences("currentWeek", position + 1)) {
							currentweek.setText("第" + (position + 1) + "周");
							Toast.makeText(SettingInfo.this, "设置当前周为第" + (position + 1) + "周", Toast.LENGTH_SHORT)
									.show();
						}
					}
					popwindow.showPopWindow();
				}
			});
			WindowManager wm = getWindowManager();
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, wm.getDefaultDisplay().getHeight() / 2);
			listview.setLayoutParams(params);
			popwindow = new MyPopupWindow(this, listview, findViewById(R.id.setting_set_layout), "center", false, false,
					false);
			popwindow.showPopWindow();
		} else {
			popwindow.showPopWindow();
		}
	}

	private void showTimePicker() {
		int hour = preferenceHelper.getIntPreferences("timePicker_hour");
		int minute = preferenceHelper.getIntPreferences("timePicker_minute");
		if (pickerDialog == null) {
			View view = getLayoutInflater().inflate(R.layout.setting_set_timedialog, null);
			final TimePicker picker = (TimePicker) view.findViewById(R.id.set_dialog_timePicker);
			picker.setIs24HourView(true);
			settime = (TextView) view.findViewById(R.id.set_dialog_settime);
			settime.setText("当前通知时间为" + hour + ":" + minute);
			Button cancel = (Button) view.findViewById(R.id.set_dialog_cancel);
			Button pick = (Button) view.findViewById(R.id.set_dialog_pick);
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pickerDialog.dismiss();
				}
			});
			pick.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Calendar c = Calendar.getInstance();
					// c.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
					// c.set(Calendar.MINUTE, picker.getCurrentMinute());
					// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
					// HH-mm-ss");
					// System.out.println("" + c.getTimeInMillis());
					// System.out.println(sdf.format(new
					// Date(c.getTimeInMillis())));
					if (preferenceHelper != null) {
						if (preferenceHelper.saveIntPreferences("timePicker_hour", picker.getCurrentHour())
								&& preferenceHelper.saveIntPreferences("timePicker_minute",
										picker.getCurrentMinute())) {
							settime.setText("当前通知时间为" + picker.getCurrentHour() + ":" + picker.getCurrentMinute());
							if (preferenceHelper.getBooleanPreferences("alarmState")) {
								SyllabusAlarm.openAlarm(SettingInfo.this);
								Toast.makeText(SettingInfo.this, "课程通知闹钟已经更改", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(SettingInfo.this,
										"课程通知时间改为" + picker.getCurrentHour() + "点" + picker.getCurrentMinute() + "分",
										Toast.LENGTH_SHORT).show();
							}
						}
					}
					pickerDialog.dismiss();
				}
			});
			pickerDialog = new AlertDialog.Builder(this).create();
			pickerDialog.show();
			WindowManager wm = pickerDialog.getWindow().getWindowManager();
			WindowManager.LayoutParams params = pickerDialog.getWindow().getAttributes();
			params.width = Math.min(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight()) * 2 / 3;
			params.height = params.WRAP_CONTENT;
			pickerDialog.getWindow().setAttributes(params);
			pickerDialog.setCancelable(true);
			pickerDialog.setContentView(view);
		} else {
			pickerDialog.show();
		}
	}

	@Override
	public void finish() {
		overridePendingTransition(0, R.anim.slide_left_out);
		super.finish();
	}

}
