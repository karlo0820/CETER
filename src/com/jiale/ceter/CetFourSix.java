package com.jiale.ceter;

import com.jiale.bean.CET;
import com.jiale.util.MyService;
import com.jiale.view.MyprogressDialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CetFourSix extends Activity implements OnClickListener {
	protected static final int SUCCESS = 0;
	protected static final int FAIL = 1;
	protected static final int NAME_ERROR = 2;
	protected static final int ID_ERROR = 3;
	private EditText cetID;
	private EditText cetName;
	private TextView check;
	private ImageButton back;
	private RelativeLayout cet_show_layout;
	private CetShowFragment cetshow;
	private MyprogressDialog progressdialog;
	private CET cet;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				changeView(2);
				showData();
				break;
			case FAIL:
				changeView(2);
				Toast.makeText(CetFourSix.this, "请求连接错误", Toast.LENGTH_SHORT).show();
				break;
			case ID_ERROR:
				changeView(2);
				Toast.makeText(CetFourSix.this, "准考证号错误", Toast.LENGTH_SHORT).show();
				break;
			case NAME_ERROR:
				changeView(2);
				Toast.makeText(CetFourSix.this, "姓名输入错误", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_of_cet);
		initView();
		initEvent();
	}

	private void initView() {
		cet = new CET();
		cetID = (EditText) findViewById(R.id.cet_code);
		cetName = (EditText) findViewById(R.id.cet_name);
		check = (TextView) findViewById(R.id.tool_item_dopost);
		check.setText("查询");
		back = (ImageButton) findViewById(R.id.tool_item_back);
		cet_show_layout = (RelativeLayout) findViewById(R.id.cet_show_layout);
		progressdialog = MyprogressDialog.createDialog(CetFourSix.this);
	}

	private void initEvent() {
		back.setOnClickListener(this);
		check.setOnClickListener(this);
	}

	private void changeView(int flag) {
		switch (flag) {
		case 1:
			progressdialog.setText("正在查询...");
			progressdialog.startDialog();
			break;
		case 2:
			progressdialog.endDialog();
			break;
		default:
			break;
		}
	}

	private void showData() {
		if (cetshow == null) {
			cetshow = new CetShowFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("cet", cet);
		cetshow.setArguments(bundle);
		getFragmentManager().beginTransaction().replace(R.id.cet_show_layout, cetshow).commit();
	}

	private void doPost() {
		final String cetid = cetID.getText().toString();
		final String cetname = cetName.getText().toString();
		new Thread() {
			@Override
			public void run() {
				Message message = new Message();
				String html = MyService.postfs(cetid, cetname);
				if (html.equals("2")) {
					message.what = ID_ERROR;
					handler.sendMessage(message);
				} else if (html.equals("4")) {
					message.what = NAME_ERROR;
					handler.sendMessage(message);
				} else if (html != null) {
					message.what = SUCCESS;
					handler.sendMessage(message);
					String[] data = html.split(",");
					if (data.length > 6) {
						cet.setCetid(cetid);
						cet.setLevel(data[0]);
						cet.setHearing(data[1]);
						cet.setReading(data[2]);
						cet.setWriting(data[3]);
						cet.setTotalpoints(data[4]);
						cet.setSchool(data[5]);
						cet.setStudent(data[6]);
						// 返回4 名字错误 返回2 准考证错误
					}
				} else {
					message.what = FAIL;
					handler.sendMessage(message);
				}
			}
		}.start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tool_item_back:
			finish();
			break;
		case R.id.tool_item_dopost:
			doPost();
			changeView(1);
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
