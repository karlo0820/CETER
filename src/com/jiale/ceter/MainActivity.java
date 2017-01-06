package com.jiale.ceter;

import java.util.ArrayList;
import java.util.List;

import com.jiale.adapter.PopupwindowAdapter;
import com.jiale.bean.ServiceBind;
import com.jiale.util.CacheUtil;
import com.jiale.util.NetWorkStateService;
import com.jiale.util.NetWorkStateService.NetWorkState;
import com.jiale.util.SharedPreferencesHelper;
import com.jiale.view.MyPopupWindow;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private ViewPager mViewPager;

	private FragmentPagerAdapter myAdapter;
	private List<Fragment> mFragment;

	private LinearLayout mXygglayout;
	private LinearLayout mToolslayout;
	private LinearLayout mSettinglayout;
	private ImageView mXyggImg;
	private ImageView mToolsImg;
	private ImageView mSettingImg;
	private ImageButton moreThing;
	private TextView xyggText;
	private TextView toolsText;
	private TextView settingText;
	private MyPopupWindow popupWindow;
	private PopupwindowAdapter adapter;
	private ListView listview;
	private List<String> arr;
	private Intent intent;
	private NetWorkStateService receiveService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		setSelect(1);
		if (new SharedPreferencesHelper(this, "setting").getBooleanPreferences("netService")) {
			bind();
		}
	}

	private void initEvent() {
		mXygglayout.setOnClickListener(this);
		mToolslayout.setOnClickListener(this);
		mSettinglayout.setOnClickListener(this);
		moreThing.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		ServiceBind.main = this;
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mXygglayout = (LinearLayout) findViewById(R.id.tab_xxzz_layout);
		mToolslayout = (LinearLayout) findViewById(R.id.tab_tools_layout);
		mSettinglayout = (LinearLayout) findViewById(R.id.tab_setting_layout);
		mXyggImg = (ImageView) findViewById(R.id.tab_xxzz_imgbtn);
		mToolsImg = (ImageView) findViewById(R.id.tab_tools_imgbtn);
		mSettingImg = (ImageView) findViewById(R.id.tab_setting_imgbtn);
		moreThing = (ImageButton) findViewById(R.id.main_tab_more);
		xyggText = (TextView) findViewById(R.id.tab_xxzz_text);
		toolsText = (TextView) findViewById(R.id.tab_tools_text);
		settingText = (TextView) findViewById(R.id.tab_setting_text);
		mFragment = new ArrayList<Fragment>();
		Fragment fTools = new ToolsFragment();
		Fragment fSetting = new SettingFragment();
		Fragment fCenter = new WyuJWFragment();
		mFragment.add(fTools);
		mFragment.add(fCenter);
		mFragment.add(fSetting);
		myAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFragment.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mFragment.get(arg0);
			}
		};
		mViewPager.setAdapter(myAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mViewPager.getCurrentItem();
				setSelect(currentItem);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void setSelect(int i) {
		// 设置按钮颜色
		// 设置内容区域
		selectTab(i);
		mViewPager.setCurrentItem(i);
	}

	private void selectTab(int i) {
		resetDrawable();
		switch (i) {
		case 0:
			toolsText.setTextColor(getResources().getColor(R.color.white));
			mToolsImg.setBackgroundResource(R.drawable.main_tools_do);
			break;
		case 1:
			xyggText.setTextColor(getResources().getColor(R.color.white));
			mXyggImg.setBackgroundResource(R.drawable.main_remind_do);
			break;
		case 2:
			settingText.setTextColor(getResources().getColor(R.color.white));
			mSettingImg.setBackgroundResource(R.drawable.main_setting_do);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tab_tools_layout:
			setSelect(0);
			break;
		case R.id.tab_xxzz_layout:
			setSelect(1);
			break;
		case R.id.tab_setting_layout:
			setSelect(2);
			break;
		case R.id.main_tab_more:
			bindAccountShow();
			break;
		default:
			break;
		}
	}

	private void bindAccountShow() {
		Log.i("bindAccount", "show");
		if (popupWindow == null) {
			Log.i("bindAccount", "popupwindow--->null");
			listview = new ListView(this);
			arr = new ArrayList<String>(1);
			if (CacheUtil.isFileExist(this, "account")) {
				arr.add("重新绑定子系统账号");
				adapter = new PopupwindowAdapter(this, arr);
				Log.i("bindAccount", "rebind");
			} else {
				arr.add("未绑定子系统账号");
				adapter = new PopupwindowAdapter(this, arr);
			}
			listview.setAdapter(adapter);
			listview.setLayoutParams(
					new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
			listview.setSelector(R.drawable.listview);
			listview.setFocusable(true);
			listview.setItemsCanFocus(true);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					System.out.println(popupWindow.isShowing());
					Intent intent = new Intent(MainActivity.this, BindAccount.class);
					startActivity(intent);
					popupWindow.showPopWindow();
				}
			});
			popupWindow = new MyPopupWindow(MainActivity.this, listview, mViewPager, "center", false, true, false);
		} else {
			Log.i("bindAccount", "popupwindow--->NOnull");
			if (CacheUtil.isFileExist(this, "account")) {
				if (arr.contains("未绑定子系统账号")) {
					arr.clear();
					arr.add("重新绑定子系统账号");
				}
			} else {
				if (arr.contains("重新绑定子系统账号")) {
					arr.clear();
					arr.add("未绑定子系统账号");
				}
			}
			adapter.notifyDataSetChanged();
		}
		popupWindow.showPopWindow();
	}

	public void bind() {
		intent = new Intent(MainActivity.this, NetWorkStateService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		Log.i("SERVICE", "绑定服务");
	}

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("SERVICE", "serviceconnected");
			receiveService = ((NetWorkStateService.MyBinder) service).getService();
			receiveService.setNetWorkState(new NetWorkState() {
				@Override
				public void getState(int flat) {
					switch (flat) {
					case 1:
						Toast.makeText(MainActivity.this, "当前网络为非WIFI状态,可能会产生额外流量损耗", Toast.LENGTH_LONG).show();
						break;
					case 2:
						Toast.makeText(MainActivity.this, "无网络连接，请连接网络", Toast.LENGTH_LONG).show();
						break;
					}
				}
			});
		}
	};

	public void unBind() {
		if (receiveService != null) {
			Log.i("SERVICE", "解除绑定");
			unbindService(connection);
			stopService(intent);
		} else {
			Log.i("SERVICE", "空");
		}
	}

	private void resetDrawable() {
		xyggText.setTextColor(getResources().getColor(R.color.black));
		toolsText.setTextColor(getResources().getColor(R.color.black));
		settingText.setTextColor(getResources().getColor(R.color.black));
		mXyggImg.setBackgroundResource(R.drawable.main_remind);
		mToolsImg.setBackgroundResource(R.drawable.main_tools);
		mSettingImg.setBackgroundResource(R.drawable.main_setting);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unBind();
	}
}
