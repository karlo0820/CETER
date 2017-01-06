package com.jiale.ceter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jiale.bean.Achievement;
import com.jiale.bean.Course;
import com.jiale.bean.MyAccount;
import com.jiale.bean.Syllabus;
import com.jiale.util.CacheUtil;
import com.jiale.util.GetCodeUtil;
import com.jiale.util.JxHtml;
import com.jiale.util.MyService;
import com.jiale.view.MyprogressDialog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class StudentCJ extends Activity implements OnClickListener, OnItemSelectedListener {
	protected static final int LOGONSUCCESS = 0;
	protected static final int LOGONFAIL = 1;
	protected static final int GETCJ_SUCCESS = 2;
	protected static final int GETCJ_FAIL = 3;
	protected static final int GETDATA_SUCCESS = 4;
	protected static final int GETDATA_FAIL = 5;
	private ImageButton back;
	private TextView check;
	private Spinner spinner;
	private EditText username;
	private EditText password;
	private MyprogressDialog progressDialog;
	private TableLayout cj_logon;
	private TableLayout cj_data_tab;
	private ScrollView cj_showdata;
	private float allAvgPoint;
	private List<Achievement> list;// 存储学期成绩
	private List<String> smester;// 存储学期
	private List<String> trainPlan;// 培养计划课程号
	private static Handler handler;
	private ArrayAdapter<String> adapter;
	private MyAccount ma;
	private boolean isExist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_of_xscj);
		initView();
		initEvent();
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.cj_back);
		check = (TextView) findViewById(R.id.cj_check);
		username = (EditText) findViewById(R.id.cj_username);
		password = (EditText) findViewById(R.id.cj_password);
		cj_logon = (TableLayout) findViewById(R.id.cj_logon_layout);
		progressDialog = MyprogressDialog.createDialog(StudentCJ.this);
		progressDialog.setText("正在请求登录...");
		spinner = (Spinner) findViewById(R.id.cj_spinner);
		cj_showdata = (ScrollView) findViewById(R.id.cj_datashow_layout);
		cj_data_tab = (TableLayout) findViewById(R.id.cj_datashow_tab);
		smester = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, smester);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case LOGONSUCCESS:
					progressDialog.setText("正在请求成绩...");
					break;
				case LOGONFAIL:
					changeView(1);
					progressDialog.endDialog();
					Toast.makeText(StudentCJ.this, "登录失败", Toast.LENGTH_SHORT).show();
					break;
				case GETCJ_FAIL:
					changeView(1);
					progressDialog.endDialog();
					Toast.makeText(StudentCJ.this, "获取数据失败", Toast.LENGTH_SHORT).show();
					break;
				case GETCJ_SUCCESS:
					progressDialog.setText("正在解析数据...");
					break;
				case GETDATA_SUCCESS:
					check.setText("重新查询");
					getSemeter();
					progressDialog.endDialog();
					Toast.makeText(StudentCJ.this, "获取数据成功", Toast.LENGTH_SHORT).show();
					break;
				case GETDATA_FAIL:
					changeView(1);
					progressDialog.endDialog();
					Toast.makeText(StudentCJ.this, "获取数据失败", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
		changeView(1);
		choose();
	}

	@SuppressWarnings("unchecked")
	private void choose() {
		list = (List<Achievement>) CacheUtil.getCache(StudentCJ.this, "cj");
		trainPlan = (List<String>) CacheUtil.getCache(StudentCJ.this, "trainPlan");
		// 本地有缓冲则直接显示，没有缓存但绑定了账号，则自行请求
		if (list != null && list.size() > 0 && trainPlan != null && trainPlan.size() > 0) {
			Message message = new Message();
			message.what = GETDATA_SUCCESS;
			handler.sendMessage(message);
			isExist = true;
		} else {
			ma = (MyAccount) CacheUtil.getCache(StudentCJ.this, "account");
			if (ma != null) {
				isExist = true;
				doPost();
			} else {
				isExist = false;
			}
		}
	}

	private void getSemeter() {
		smester.clear();
		smester.add("全部成绩");
		for (int i = 0; i < list.size(); i++) {
			smester.add(list.get(i).getSemester());
		}
		adapter.notifyDataSetChanged();
		isExist = true;
	}

	private void initEvent() {
		back.setOnClickListener(this);
		check.setOnClickListener(this);
		spinner.setOnItemSelectedListener(this);
	}

	@SuppressWarnings("unchecked")
	private void doPost() {
		final String usercode;
		final String userpwd;
		if (ma != null && isExist) {
			usercode = ma.getAccount();
			userpwd = ma.getPwd();
		} else {
			usercode = username.getText().toString();
			userpwd = password.getText().toString();
			if (usercode == null || usercode.equals("") || userpwd == null || userpwd.equals("")) {
				Toast.makeText(StudentCJ.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		progressDialog.startDialog();
		new Thread() {
			@Override
			public void run() {
				// 请求验证码
				Message message = new Message();
				String cookie = MyService.getCjLogin("http://jwc.wyu.edu.cn/student/rndnum.asp");
				String code = GetCodeUtil.getCode(cookie);
				cookie = GetCodeUtil.getCookie(cookie);
				// 请求登录 123失败 184成功
				String string = MyService.post(usercode, userpwd, code, cookie,
						"http://jwc.wyu.edu.cn/student/logon.asp");
				if (string.length() == 123) {
					message.what = LOGONFAIL;
					handler.sendMessage(message);
				} else {
					message.what = LOGONSUCCESS;
					handler.sendMessage(message);
					// 请求成绩页面，126失败 -----并解析
					message = new Message();
					String html = MyService.getCj("http://jwc.wyu.edu.cn/student/f4_myscore.asp");
					if (html.length() == 126 || html.length() == 0) {
						message.what = GETCJ_FAIL;
						handler.sendMessage(message);
					} else {
						message.what = GETCJ_SUCCESS;
						handler.sendMessage(message);
						message = new Message();
						list = JxHtml.cj(html);
						trainPlan = JxHtml.trainPlan(MyService.getTrainPlan());
						if (!list.isEmpty() && list.size() > 0 && !trainPlan.isEmpty() && trainPlan.size() > 0) {
							message.what = GETDATA_SUCCESS;
							handler.sendMessage(message);
							CacheUtil.setCache(StudentCJ.this, "cj", list);
							CacheUtil.setCache(StudentCJ.this, "trainPlan", trainPlan);

						} else {
							message.what = GETDATA_FAIL;
							handler.sendMessage(message);
						}
					}
				}
			}

		}.start();
	}

	private void changeView(int flag) {
		switch (flag) {
		case 1:
			check.setText("查询");
			cj_showdata.setVisibility(View.INVISIBLE);
			spinner.setVisibility(View.INVISIBLE);
			cj_logon.setVisibility(View.VISIBLE);
			break;
		case 2:
			cj_logon.setVisibility(View.INVISIBLE);
			progressDialog.endDialog();
			spinner.setVisibility(View.VISIBLE);
			cj_showdata.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cj_back:
			finish();
			break;
		case R.id.cj_check:
			if (cj_showdata.isShown() && isExist) {
				changeView(1);
			} else {
				isExist = false;
				doPost();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		cj_data_tab.removeAllViews();
		if (position == 0) {// 默认全部成绩
			for (int i = 0; i < list.size(); i++) {
				LinearLayout ly = new LinearLayout(this);
				ly.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				ly.setBackgroundResource(R.drawable.table);
				ly.setGravity(Gravity.CENTER);
				TextView tv_title = new TextView(this);
				tv_title.setTextSize(15f);
				tv_title.setText(list.get(i).getSemester());
				ly.addView(tv_title);
				cj_data_tab.addView(ly);
				showdata(list.get(i).getCourse());
			}
			if (allAvgPoint == 0.0f) {
				calculateAllGPA(selectCourse(list, 0));
			}
			TextView tv_gpa = new TextView(this);
			tv_gpa.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tv_gpa.setGravity(Gravity.CENTER);
			tv_gpa.setTextSize(15f);
			tv_gpa.setText("(只计算培养方案必修科(包括专业选修),本系统数据仅供参考,以教务处给出数据为准)毕业绩点：" + allAvgPoint);
			tv_gpa.setBackgroundResource(R.drawable.table);
			cj_data_tab.addView(tv_gpa);
		} else {
			List<Course> cl = list.get(--position).getCourse();// 单个学期成绩
			showdata(cl);
			TextView tv_gpa = new TextView(this);
			tv_gpa.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tv_gpa.setGravity(Gravity.CENTER);
			tv_gpa.setTextSize(15f);
			tv_gpa.setText("(只计算培养方案必修科(包括专业选修),本系统数据仅供参考,以教务处给出数据为准)平均绩点：" + calculateGPA(selectCourse(cl)));
			tv_gpa.setBackgroundResource(R.drawable.table);
			cj_data_tab.addView(tv_gpa);
		}
		changeView(2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	// 计算毕业绩点
	private float calculateAllGPA(Map<String, Course> data) {
		float allCredit = 0.0f;
		float allCreditPoint = 0.0f;
		Iterator<Entry<String, Course>> iter = data.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Course> entry = (Map.Entry<String, Course>) iter.next();
			Course course = entry.getValue();
			float gpa = 0.0f;
			String score = course.getScore();
			float credit = Float.valueOf(course.getCredit());
			allCredit += credit;
			if (score.equals("旷考")) {

			} else if (score.equals("优秀")) {
				gpa = 4.5f;
			} else if (score.equals("良好")) {
				gpa = 3.5f;
			} else if (score.equals("中等")) {
				gpa = 2.5f;
			} else if (score.equals("及格")) {
				gpa = 1.0f;
			} else if (score.equals("不及格")) {
				gpa = 0.0f;
			} else {
				int temp = (int) Float.parseFloat(score);
				if (temp >= 60) {
					gpa = 1 + (temp - 60) * 0.1f;
				}
			}
			allCreditPoint += gpa * credit;
		}
		if (allCredit > 0) {
			allAvgPoint = allCreditPoint / allCredit;
		}
		return allAvgPoint;
	}

	// 计算学期平均绩点
	private float calculateGPA(Map<String, Course> data) {
		float allCredit = 0.0f;// 所有学分总和
		float allCreditPoint = 0.0f;
		Iterator<Entry<String, Course>> inIterator = data.entrySet().iterator();
		while (inIterator.hasNext()) {
			Entry<String, Course> entry = (Entry<String, Course>) inIterator.next();
			Course course = (Course) entry.getValue();
			float gpa = 0.0f;
			String score = course.getScore();
			float credit = Float.valueOf(course.getCredit());
			allCredit += credit;
			if (score.equals("旷考")) {

			} else if (score.equals("优秀")) {
				gpa = 4.5f;
			} else if (score.equals("良好")) {
				gpa = 3.5f;
			} else if (score.equals("中等")) {
				gpa = 2.5f;
			} else if (score.equals("及格")) {
				gpa = 1.0f;
			} else if (score.equals("不及格")) {
				gpa = 0.0f;
			} else {
				int temp = (int) Float.parseFloat(score);
				if (temp >= 60) {
					gpa = 1 + (temp - 60) * 0.1f;
				}
			}
			allCreditPoint += gpa * credit;
		}
		float avgPoint = 0.0f;
		if (allCredit > 0) {
			avgPoint = allCreditPoint / allCredit;
		}
		return avgPoint;
	}

	// 过滤全部必修科（包括专业选修）
	private Map<String, Course> selectCourse(List<Achievement> list, int flat) {
		Map<String, Course> planCourse = new HashMap<String, Course>();
		for (Achievement am : list) {
			List<Course> data = am.getCourse();
			for (int i = 1; i < data.size(); i++) {
				if (data.get(i).getRemark().equals("已认定") || data.get(i).getRemark().equals("模块课")) {
					planCourse.put(data.get(i).getCourseNumber(), data.get(i));
					// planCourse.add(data.get(i));
				} else {
					for (int j = 0; j < trainPlan.size(); j++) {
						if (data.get(i).getCourseNumber().equals(trainPlan.get(j))) {
							planCourse.put(data.get(i).getCourseNumber(), data.get(i));
							// planCourse.add(data.get(i));
							break;
						}
						if (j == trainPlan.size() - 1) {
							Log.i("不在培养方案", data.get(i).getCourseName());
						}
					}
				}
			}
		}
		return planCourse;
	}

	// 过滤单学期必修科（包括专业选修）
	private Map<String, Course> selectCourse(List<Course> data) {
		// List<Course> planCourse = new ArrayList<Course>();
		Map<String, Course> plan = new HashMap<String, Course>();
		for (int i = 1; i < data.size(); i++) {
			if (data.get(i).getRemark().equals("已认定") || data.get(i).getRemark().equals("模块课")) {
				plan.put(data.get(i).getCourseNumber(), data.get(i));
				// planCourse.add(data.get(i));
			} else {
				for (int j = 0; j < trainPlan.size(); j++) {
					if (data.get(i).getCourseNumber().equals(trainPlan.get(j))) {
						plan.put(data.get(i).getCourseNumber(), data.get(i));
						// planCourse.add(data.get(i));
						break;
					}
					if (j == trainPlan.size() - 1) {
						Log.i("不在培养方案", data.get(i).getCourseName());
					}
				}
			}
		}
		return plan;
	}

	private void showdata(List<Course> data) {
		cj_data_tab.setStretchAllColumns(true);
		for (int i = 0; i < data.size(); i++) {
			TableRow tr = new TableRow(this);
			tr.setBackgroundColor(Color.rgb(222, 220, 210));
			tr.setMinimumHeight(30);
			for (int j = 0; j < 6; j++) {
				TextView tv = new TextView(this);
				tv.setBackgroundResource(R.drawable.table);
				tv.setTextSize(10f);
				tv.setGravity(Gravity.CENTER);
				if (data.get(i).getCourseName().length() > 15) {
					tv.setMaxLines(2);
				}
				switch (j) {
				case 0:
					tv.setText(data.get(i).getCourseNumber());
					break;
				case 1:
					tv.setText(data.get(i).getCourseName());
					break;
				case 2:
					tv.setText(data.get(i).getCategory());
					break;
				case 3:
					tv.setText(data.get(i).getCredit());
					break;
				case 4:
					tv.setText(data.get(i).getScore());
					break;
				case 5:
					tv.setText(data.get(i).getRemark());
					break;
				case 6:
					break;
				default:
					break;
				}
				tr.addView(tv);
			}
			cj_data_tab.addView(tr);
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
