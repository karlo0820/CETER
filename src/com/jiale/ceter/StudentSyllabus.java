package com.jiale.ceter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiale.adapter.SyllabusInfoAdapter;
import com.jiale.bean.MyAccount;
import com.jiale.bean.Syllabus;
import com.jiale.util.CacheUtil;
import com.jiale.util.ForCurrentWeekUtil;
import com.jiale.util.GetCodeUtil;
import com.jiale.util.JxHtml;
import com.jiale.util.MyService;
import com.jiale.util.SharedPreferencesHelper;
import com.jiale.view.MyGallery3D;
import com.jiale.view.MyPopupWindow;
import com.jiale.view.MyprogressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StudentSyllabus extends Activity implements OnClickListener {
	private final static int LOGIN_SUCCESS = 1;
	private final static int LOGIN_FAIL = 2;
	private final static int GET_KB_SUCCESS = 3;
	private final static int GET_KB_FAIL = 4;
	private final static int GET_DATA_SUCCESS = 5;
	private final static int GET_DATA_FAIL = 6;
	private List<Syllabus> list; // 存储原课表数据
	private ImageButton back;
	private TextView check;
	private EditText username;
	private EditText password;
	private LinearLayout kb_show_layout;
	private LinearLayout kb_show_time;
	private TableLayout kb_login_layut;
	private List<LinearLayout> kb_days_list; // 布局list，代表星期1-7
	private Map<Integer, List<Syllabus>> syllabusInfoMap;// 存储每个课程,key-textview的ID,value-课程list,用于处理每个textview的点击事件
	private MyprogressDialog progress;
	private ArrayAdapter<String> adapter;
	private Map<String, Integer> colorSelecter; // 颜色选择
	private Spinner kb_weeks; // 周数spinner
	private static Handler handler;
	private int textViewHeight;
	private int currentWeek;// 当前周
	private List<String> weeks;// 周数
	private boolean flat;// 缓存文件是否存在标识
	private MyAccount ma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tools_of_xskb);
		initView();
		initListener();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		currentWeek = ForCurrentWeekUtil.getCurrentWeek(new SharedPreferencesHelper(this, "setting"));
		back = (ImageButton) findViewById(R.id.kb_back);
		check = (TextView) findViewById(R.id.kb_check);
		username = (EditText) findViewById(R.id.kb_username);
		password = (EditText) findViewById(R.id.kb_password);
		kb_show_layout = (LinearLayout) findViewById(R.id.kb_show_layout);
		kb_show_time = (LinearLayout) findViewById(R.id.kb_show_time);
		kb_show_time.setMinimumHeight(100);
		int height = (getWindowManager().getDefaultDisplay().getHeight() - 50);
		kb_show_time.setLayoutParams(new LinearLayout.LayoutParams(0, height, 0.5f));
		kb_login_layut = (TableLayout) findViewById(R.id.kb_login_layout);
		textViewHeight = height / 5;
		kb_weeks = (Spinner) findViewById(R.id.kb_weeks);
		weeks = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, weeks);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.setNotifyOnChange(true);
		kb_weeks.setAdapter(adapter);
		kb_weeks.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.i("SpinnerPosition", "" + position);
				show(position + 1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		syllabusInfoMap = new HashMap<Integer, List<Syllabus>>();
		progress = MyprogressDialog.createDialog(this);
		progress.setText("正在请求登录...");
		kb_days_list = new ArrayList<LinearLayout>(7);
		for (int i = 0; i < 7; i++) {
			kb_days_list.add((LinearLayout) findViewById(R.id.kb_show_day_1 + i));
		}
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LOGIN_SUCCESS:
					progress.setText("正在请求课表...");
					break;
				case LOGIN_FAIL:
					Toast.makeText(StudentSyllabus.this, "登录失败", Toast.LENGTH_SHORT).show();
					progress.endDialog();
					break;
				case GET_KB_SUCCESS:
					progress.setText("正在解析课表...");
					break;
				case GET_KB_FAIL:
					Toast.makeText(StudentSyllabus.this, "请求课表失败", Toast.LENGTH_SHORT).show();
					progress.endDialog();
					break;
				case GET_DATA_SUCCESS:
					Toast.makeText(StudentSyllabus.this, "获取课表成功", Toast.LENGTH_SHORT).show();
					progress.endDialog();
					setWeeks();
					break;
				case GET_DATA_FAIL:
					Toast.makeText(StudentSyllabus.this, "获取课表数据失败", Toast.LENGTH_SHORT).show();
					progress.endDialog();
					break;
				default:
					break;
				}
			}

		};
		choose();
	}

	private void initListener() {
		back.setOnClickListener(this);
		check.setOnClickListener(this);
	}

	private void changeView(int i) {
		switch (i) {
		case 1:
			kb_login_layut.setVisibility(View.VISIBLE);
			kb_show_layout.setVisibility(View.INVISIBLE);
			check.setText("查询");
			kb_weeks.setVisibility(View.INVISIBLE);
			break;
		case 2:
			kb_login_layut.setVisibility(View.INVISIBLE);
			kb_show_layout.setVisibility(View.VISIBLE);
			kb_weeks.setVisibility(View.VISIBLE);
			check.setText("重新查询");
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void choose() {
		list = (List<Syllabus>) CacheUtil.getCache(StudentSyllabus.this, "syllabus");
		// 本地有缓冲则直接显示，没有缓存但绑定了账号，则自行请求
		if (list != null && list.size() > 0) {
			Message message = new Message();
			message.what = GET_DATA_SUCCESS;
			handler.sendMessage(message);
			check.setText("重新查询");
			flat = true;
		} else {
			ma = (MyAccount) CacheUtil.getCache(StudentSyllabus.this, "account");
			if (ma != null) {
				flat = true;
				changeView(1);
				postKb();
			} else {
				flat = false;
				changeView(1);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.kb_back:
			finish();
			break;
		case R.id.kb_check:
			if (kb_show_layout.isShown() && flat) {
				clearView();
				changeView(1);
			} else {
				flat = false;
				postKb();
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void postKb() {
		final String usercode;
		final String userpwd;
		if (ma != null && flat) {
			usercode = ma.getAccount();
			userpwd = ma.getPwd();
		} else {
			usercode = username.getText().toString();
			userpwd = password.getText().toString();
			if (usercode == null || usercode.equals("") || userpwd == null || userpwd.equals("")) {
				Toast.makeText(StudentSyllabus.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		progress.startDialog();
		new Thread() {
			@Override
			public void run() {
				String cookie = MyService.getCjLogin("http://jwc.wyu.edu.cn/student/rndnum.asp");
				String code = GetCodeUtil.getCode(cookie);
				cookie = GetCodeUtil.getCookie(cookie);
				// 请求登录
				String string = MyService.post(usercode, userpwd, code, cookie,
						"http://jwc.wyu.edu.cn/student/logon.asp");
				// 请求登录 123失败 184 216--密码强度弱
				Message message = new Message();
				if (string.length() == 123) {
					message.what = LOGIN_FAIL;
					handler.sendMessage(message);
				} else {
					message.what = LOGIN_SUCCESS;
					handler.sendMessage(message);
					message = new Message();
					String html = MyService.get("http://jwc.wyu.edu.cn/student/f3.asp");
					if (html.length() == 126 || html.length() == 0) {
						message.what = GET_KB_FAIL;
						handler.sendMessage(message);
					} else {
						message.what = GET_KB_SUCCESS;
						handler.sendMessage(message);
						message = new Message();
						list = JxHtml.kb(html);
						if (list != null && list.size() > 0 && !list.isEmpty()) {
							message.what = GET_DATA_SUCCESS;
							handler.sendMessage(message);
							CacheUtil.setCache(StudentSyllabus.this, "syllabus", list);
						} else {
							message.what = GET_DATA_FAIL;
							handler.sendMessage(message);
						}
					}
				}
			}

		}.start();

	}

	private void show(int week) {
		clearView();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, textViewHeight);
		int index = 1;// textview index
		int colorindex = 0;// 颜色选择index
		int[] background = { R.drawable.kb_textview_1, R.drawable.kb_textview_2, R.drawable.kb_textview_3,
				R.drawable.kb_textview_4, R.drawable.kb_textview_5, R.drawable.kb_textview_6, R.drawable.kb_textview_7,
				R.drawable.kb_textview_8, R.drawable.kb_textview_9, R.drawable.kb_textview_10,
				R.drawable.kb_textview_11, R.drawable.kb_textview_12 };
		colorSelecter = new HashMap<String, Integer>();
		for (int day = 0; day < 7; day++) {
			LinearLayout ll = kb_days_list.get(day);
			for (int jie = 1; jie < 6; jie++) {
				index++;
				TextView tv = new TextView(this);
				tv.setLayoutParams(lp);
				tv.setTextSize(10.0f);
				tv.setId((day + 1) * 100 + jie * 10 + index);
				String text = new String();
				List<Syllabus> daySyllabusInfo = new ArrayList<Syllabus>();
				for (int j = 0; j < list.size(); j++) {
					Syllabus s = list.get(j);
					if (s.getDay() == day + 1 && s.getJie() == jie) {
						daySyllabusInfo.add(s);
						text = s.getCourseName() + "@" + s.getAddress();
						// 判断是否在当前周
						if ((s.getStartUP() <= week && s.getEndUP() >= week)
								|| (s.getStartDown() <= week && s.getEndDown() >= week)
								|| (s.getStartUP() == week && s.getEndUP() == 0)) {
							if (colorSelecter.containsKey(s.getCourseName())) {
								tv.setBackgroundResource(colorSelecter.get(s.getCourseName()));
							} else {
								tv.setBackgroundResource(background[colorindex]);
								Log.i("color", "" + background[colorindex]);
								colorSelecter.put(s.getCourseName(), background[colorindex]);
								colorindex++;
							}
						} else {
							tv.setBackgroundResource(R.drawable.kb_textview);
						}
					}
				}
				if (text.length() > 0) {
					tv.setText(text);
					syllabusInfoMap.put(tv.getId(), daySyllabusInfo);
					tv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.i("textviewID", "" + v.getId());
							if (syllabusInfoMap.containsKey(v.getId())) {
								final List<Syllabus> clickList = syllabusInfoMap.get(v.getId());
								Log.i("textviewID", "" + clickList.toString());
								if (clickList.size() > 1) {
									LayoutInflater inflater = (LayoutInflater) getSystemService(
											Context.LAYOUT_INFLATER_SERVICE);
									View gallertView = inflater.inflate(R.layout.tools_of_xskb_gallery3d, null);
									final Dialog dialog = new AlertDialog.Builder(StudentSyllabus.this).create();
									dialog.setCanceledOnTouchOutside(true);
									dialog.setCancelable(true);
									dialog.show();
									WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
									params.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
									params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
									dialog.getWindow().setAttributes(params);
									SyllabusInfoAdapter adapter = new SyllabusInfoAdapter(StudentSyllabus.this,
											clickList, colorSelecter);
									MyGallery3D gallery = (MyGallery3D) gallertView.findViewById(R.id.kb_gallery);
									gallery.setAdapter(adapter);
									dialog.addContentView(gallertView,
											new LayoutParams(android.widget.Gallery.LayoutParams.MATCH_PARENT,
													android.widget.Gallery.LayoutParams.WRAP_CONTENT));
									gallery.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(AdapterView<?> parent, View view, int position,
												long id) {
											System.out.println(clickList.get(position).toString());
											Intent intent = new Intent(StudentSyllabus.this, SyllabusInfo.class);
											Bundle bundle = new Bundle();
											bundle.putSerializable("syllabusInfo", clickList.get(position));
											intent.putExtra("info", bundle);
											startActivityForResult(intent, 2);
											dialog.dismiss();

										}
									});
								} else {
									Intent intent = new Intent(StudentSyllabus.this, SyllabusInfo.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("syllabusInfo", clickList.get(0));
									intent.putExtra("info", bundle);
									startActivityForResult(intent, 2);
								}
							}
						}
					});
				} else {
					tv.setBackgroundResource(R.drawable.kb_textview_none);
					tv.setText(" ");
				}
				ll.addView(tv);
			}
		}
		changeView(2);
	}

	private void setWeeks() {
		flat = true;
		if (weeks.isEmpty()) {
			for (int i = 1; i <= 20; i++) {
				weeks.add("第" + i + "周");
			}
		}
		adapter.notifyDataSetChanged();
		kb_weeks.setSelection(currentWeek - 1);
	}

	private void clearView() {
		for (int i = 0; i < kb_days_list.size(); i++) {
			kb_days_list.get(i).removeAllViews();
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
