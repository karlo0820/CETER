package com.jiale.ceter;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiale.util.JxHtml;
import com.jiale.util.MyService;

public class CheckBattery extends FragmentActivity implements OnClickListener {
	protected final static int SUCCESS = 1;
	protected final static int FAIL = 2;
	protected static final int FAIL2 = 3;
	private EditText buildID;
	private EditText apartID;
	private TextView check;
	private LinearLayout progress;
	private RelativeLayout battery_layout;
	private TableLayout batter_datashow_layout;
	private ImageButton back;
	private ArrayList<String> data;
	private BatteryDataFragment bsd;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				changeView(1);
				showData();
				break;
			case FAIL:
				changeView(3);
				break;
			case FAIL2:
				changeView(4);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_of_battery);
		inintView();
		initEvent();
	}

	private void initEvent() {
		back.setOnClickListener(this);
		check.setOnClickListener(this);
	}

	private void inintView() {
		buildID = (EditText) findViewById(R.id.battery_building);
		apartID = (EditText) findViewById(R.id.battery_apartID);
		check = (TextView) findViewById(R.id.tool_item_dopost);
		check.setText("查询");
		batter_datashow_layout = (TableLayout) findViewById(R.id.batter_datashow_layout);
		battery_layout = (RelativeLayout) findViewById(R.id.battery_layout);
		progress = (LinearLayout) findViewById(R.id.battery_progress_layout);
		back = (ImageButton) findViewById(R.id.tool_item_back);
	}

	private void changeView(int flag) {
		switch (flag) {
		case 1:
			progress.setVisibility(View.INVISIBLE);
			break;
		case 2:
			progress.setVisibility(View.VISIBLE);
			break;
		case 3:
			progress.setVisibility(View.INVISIBLE);
			Toast.makeText(this, "输入的为无效房间编号", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			progress.setVisibility(View.INVISIBLE);
			Toast.makeText(this, "此栋楼不能智能查电", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void doPost() {
		boolean flag = false;
		String[] sentbuild = new String[] { "1", "9", "17", "25", "33", "40", "47", "55", "63", "68", "74", "75", "76",
				"77", "105", "114", "122", "123", "124", "214", "226", "227", "251", "252", "267", "277", "278", "279",
				"314", "315" };
		String[] typebuild = new String[] { "34", "35", "36", "37", "1", "2", "3", "4", "5", "6", "19", "20", "21",
				"22", "14", "15", "38", "39", "40", "41", "42", "43", "16", "17", "18", "44", "45", "46", "47", "48" };
		data = new ArrayList<String>();
		String build = buildID.getText().toString();
		String apart = apartID.getText().toString();
		data.add(build);
		data.add(apart);
		for (int i = 0; i < typebuild.length; i++) {
			if (build.equals(typebuild[i])) {
				apart = build + apart;
				build = sentbuild[i];
				flag = true;
			}
		}
		final String dobuild = build;
		final String doapart = apart;
		if (flag) {
			new Thread() {
				@Override
				public void run() {
					Message message = new Message();
					MyService.doGet("http://202.192.252.140");
					data.addAll(data.size(), JxHtml
							.battery(MyService.postBattery("http://202.192.252.140/index.asp", doapart, dobuild)));
					if (data != null && data.size() > 2) {
						message.what = SUCCESS;
					} else {
						message.what = FAIL;

					}
					handler.sendMessage(message);
				}
			}.start();
		} else {
			Message message = new Message();
			message.what = FAIL2;
			handler.sendMessage(message);
		}
	}

	private void showData() {
		if (bsd == null) {
			bsd = new BatteryDataFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("data", data);
		bsd.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.battery_layout, bsd).commit();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tool_item_back) {
			finish();
		} else {
			doPost();
			changeView(2);
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
