package com.jiale.ceter;

import java.util.ArrayList;
import java.util.List;

import com.jiale.adapter.LibsAdapter;
import com.jiale.bean.Books;
import com.jiale.util.GetClient;
import com.jiale.util.GetImageCode;
import com.jiale.util.JxHtml;
import com.jiale.util.MyService;
import com.jiale.view.MyprogressDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LibsActivity extends Activity implements OnClickListener {
	private static String TAG = "LibsActivity";
	private final static int CODE_SUCCESS = 1;
	private final static int CODE_FAIL = 2;
	private final static int LOGON_SUCCESS = 3;
	private final static int LOGON_FAIL = 4;
	private final static int GETLIBS_SUCCESS = 5;
	private final static int GETLIBS_FIAL = 6;
	private final static int GETDATA_SUCCESS = 7;
	private final static int GETDATA_FAIL = 8;
	private final static int SHOW_SUCCESS = 9;
	private final static int EXBOOK_SUCCESS = 10;
	private final static int EXBOOK_FAIL = 11;
	private final static int NETWORK_WRONG = 12;
	private List<String> data;
	private TableLayout logonTab;
	private LinearLayout nodata;
	private MyprogressDialog progressdialog;
	private EditText userNum;
	private EditText userPwd;
	private EditText passCode;
	private ImageView passImg;
	private TextView reCode;
	private TextView post;
	private ImageButton back;
	private Bitmap bitmap;
	private String html;
	private LibsAdapter adapter;
	private ListView listview;
	private List<Books> mdata;
	private AlertDialog dialog;
	private TextView content;
	private String state;
	private String dation;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NETWORK_WRONG:
				Toast.makeText(LibsActivity.this, "网络出现问题", Toast.LENGTH_SHORT).show();
				changeView(1);
				break;
			case CODE_SUCCESS:
				passImg.setImageBitmap(bitmap);
				break;
			case CODE_FAIL:
				Log.i(TAG, "获取验证码失败");
				break;
			case LOGON_SUCCESS:
				progressdialog.setText("正在请求借阅页面...");
				break;
			case LOGON_FAIL:
				Toast.makeText(LibsActivity.this, "请求登录失败", Toast.LENGTH_SHORT).show();
				changeView(1);
				break;
			case GETLIBS_SUCCESS:
				progressdialog.setText("正在解析数据...");
				break;
			case GETLIBS_FIAL:
				Toast.makeText(LibsActivity.this, "请求借阅情况失败", Toast.LENGTH_SHORT).show();
				changeView(1);
				break;
			case GETDATA_SUCCESS:
				progressdialog.setText("正在初始化视图...");
				showData();
				break;
			case GETDATA_FAIL:
				Toast.makeText(LibsActivity.this, "获取数据成功", Toast.LENGTH_SHORT).show();
				changeView(3);
				break;
			case SHOW_SUCCESS:
				Toast.makeText(LibsActivity.this, "获取数据成功", Toast.LENGTH_SHORT).show();
				changeView(2);
				break;
			case EXBOOK_SUCCESS:
				adapter.notifyDataSetChanged();
				Toast.makeText(LibsActivity.this, "续借成功", Toast.LENGTH_SHORT).show();
				break;
			case EXBOOK_FAIL:
				Toast.makeText(LibsActivity.this, "续借失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_of_libs);
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		logonTab = (TableLayout) findViewById(R.id.logon_table);
		nodata = (LinearLayout) findViewById(R.id.nodata);
		progressdialog = MyprogressDialog.createDialog(LibsActivity.this);
		userNum = (EditText) findViewById(R.id.usernum);
		userPwd = (EditText) findViewById(R.id.userpwd);
		passCode = (EditText) findViewById(R.id.code);
		passImg = (ImageView) findViewById(R.id.codeimg);
		post = (TextView) findViewById(R.id.tool_item_dopost);
		post.setText("查询");
		back = (ImageButton) findViewById(R.id.tool_item_back);
		reCode = (TextView) findViewById(R.id.recode);
		data = new ArrayList<String>();
		listview = (ListView) findViewById(R.id.libs_listview);
		changeView(1);
	}

	private void initData() {
		new Thread() {
			@Override
			public void run() {
				Message message = new Message();
				String gethtml = MyService
						.getLib("http://lib.wyu.edu.cn/opac/login.aspx?ReturnUrl=%2fopac%2fuser%2fchangepas.aspx");
				if (gethtml == null) {
					message.what = NETWORK_WRONG;
				} else {
					String[] string = JxHtml.getLibPostData(gethtml);
					if (string != null) {
						dation = string[0];
						state = string[1];
					}
					bitmap = GetImageCode.getBitmap("http://lib.wyu.edu.cn/opac/gencheckcode.aspx?rd=" + Math.random());
					if (bitmap != null) {
						message.what = CODE_SUCCESS;
					} else {
						message.what = CODE_FAIL;
					}
				}
				handler.sendMessage(message);
			}
		}.start();
	}

	private void initEvent() {
		post.setOnClickListener(this);
		back.setOnClickListener(this);
		reCode.setOnClickListener(this);
	}


	private void changeView(int flag) {
		switch (flag) {
		case 1:
			logonTab.setVisibility(View.VISIBLE);
			nodata.setVisibility(View.GONE);
			listview.setVisibility(View.GONE);
			progressdialog.setText("正在请求登录...");
			progressdialog.endDialog();
			break;
		case 2:
			logonTab.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			nodata.setVisibility(View.GONE);
			progressdialog.endDialog();
			break;
		case 3:
			logonTab.setVisibility(View.GONE);
			listview.setVisibility(View.GONE);
			nodata.setVisibility(View.VISIBLE);
			progressdialog.endDialog();
			break;
		case 4:
			logonTab.setVisibility(View.GONE);
			listview.setVisibility(View.GONE);
			nodata.setVisibility(View.GONE);
			progressdialog.startDialog();
			break;
		default:
			break;
		}
	}

	private void showData() {
		if (data != null && data.size() > 2) {
			Message message = new Message();
			String[] item = null;
			mdata = new ArrayList<Books>();
			for (int i = 2; i < data.size(); i++) {
				item = data.get(i).split("=");
				Books book = new Books();
				if (item[0].length() == 0) {
					book.setFlat("可续借");
				} else {
					book.setFlat(item[0]);
				}
				book.setLast(item[1]);
				book.setBookName(item[2]);
				book.setLease(item[3]);
				mdata.add(book);
			}
			adapter = new LibsAdapter(this, mdata);
			listview.setAdapter(adapter);
			listview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
					Log.i("LIBS", position + "position");
					showMyDialog(position);
					return false;
				}

			});
			message.what = SHOW_SUCCESS;
			handler.sendMessage(message);
		} else {
			changeView(3);
		}

	}

	private void doLogon() {
		final String usernum = userNum.getText().toString();
		final String userpwd = userPwd.getText().toString();
		final String passcode = passCode.getText().toString();
		if ((!usernum.equals("")) && (!userpwd.equals("")) && (!passcode.equals(""))) {
			changeView(4);
			new Thread() {
				@Override
				public void run() {
					Message message = new Message();
					if (MyService
							.postLib(usernum, userpwd, dation, state, passcode, "http://lib.wyu.edu.cn/opac/login.aspx")
							.length() != 16719)
					// 登录成功=14363,登录失败=16719
					{
						message.what = LOGON_SUCCESS;
						handler.sendMessage(message);
						html = MyService.doGet("http://lib.wyu.edu.cn/opac/user/bookborrowed.aspx");
						Log.i("借阅情况页面", html);
						Message get_message = new Message();
						if (html.contains("当前借阅情况")) {
							get_message.what = GETLIBS_SUCCESS;
							handler.sendMessage(get_message);
							Message dataMessage = new Message();
							data = JxHtml.wyuLib(html);
							if (data != null && data.size() <= 1) {
								dataMessage.what = GETDATA_FAIL;
							} else {
								dataMessage.what = GETDATA_SUCCESS;
							}
							handler.sendMessage(dataMessage);
						} else {
							get_message.what = GETLIBS_FIAL;
							handler.sendMessage(get_message);
						}
					} else {
						message.what = LOGON_FAIL;
						handler.sendMessage(message);
					}

				}
			}.start();

		} else {
			Toast.makeText(LibsActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
		}
	}

	private void showMyDialog(final int position) {
		if (mdata.get(position).getFlat().equals("续满")) {
			return;
		}
		if (dialog == null) {
			dialog = new AlertDialog.Builder(this).create();
			dialog.show();
			View view = LayoutInflater.from(this).inflate(R.layout.setting_cache_dialog, null);
			content = (TextView) view.findViewById(R.id.dialog_message);
			Button send = (Button) view.findViewById(R.id.dialog_clear);
			Button cancel = (Button) view.findViewById(R.id.dialog_cancel);
			content.setText(mdata.get(position).getBookName());
			send.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new Thread() {
						@Override
						public void run() {
							dation = data.get(0);
							state = data.get(1);
							String html = MyService.postExBooks(position + 100, dation, state);
							Message message = new Message();
							if (html.contains("续借成功")) {
								// 更改本地获取的数据。
								Books book = mdata.get(position);
								book.setFlat("续满");
								mdata.set(position, book);
								message.what = EXBOOK_SUCCESS;
							} else {
								message.what = EXBOOK_FAIL;
							}
							handler.sendMessage(message);
						}
					}.start();
					dialog.dismiss();
				}
			});
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			WindowManager wm = dialog.getWindow().getWindowManager();
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			Point point = new Point();
			wm.getDefaultDisplay().getSize(point);
			params.width = Math.min(point.x, point.y) * 2 / 3;
			params.height = LayoutParams.WRAP_CONTENT;
			dialog.getWindow().setAttributes(params);
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(true);
		} else {
			content.setText(mdata.get(position).getBookName());
			dialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tool_item_back:
			finish();
			break;
		case R.id.tool_item_dopost:
			if (logonTab.getVisibility() == View.VISIBLE) {
				progressdialog.setText("正在请求登录");
				doLogon();
			} else {
				Toast.makeText(this, "请勿重复查询", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.recode:
			initData();
			break;
		default:
			break;
		}
	}

	@Override
	public void finish() {
		progressdialog = null;
		if (GetClient.httpClient != null) {
			GetClient.httpClient = null;
		}
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
