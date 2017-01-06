package com.jiale.ceter;

import java.util.List;

import com.jiale.adapter.RefreshViewAdapter;
import com.jiale.bean.WyuNew;
import com.jiale.util.CacheUtil;
import com.jiale.util.JxHtml;
import com.jiale.util.WyuNews;
import com.jiale.view.MyprogressDialog;
import com.jiale.view.OnRefreshListener;
import com.jiale.view.RefreshListView;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WyuJWFragment extends Fragment implements OnRefreshListener {
	private static final int DATASUCCESS = 1;
	private static final int DATAFAIL = 2;
	private static final int VIEWSUCCESS = 3;
	private static final int VIEWFAIL = 4;
	private static final int ONLOADING_ERROR = 5;
	private static final int REFRESHING_ERROR = 6;
	private static int page = 1;
	private static String JwPage = "http://jwc.wyu.edu.cn/www/newslist.asp?id=51";
	private static String JwURL = "http://jwc.wyu.edu.cn/www/newslist.asp?id=51&skey=&pg=";
	private boolean isNew;
	private View view;
	private RefreshListView listview;
	private RefreshViewAdapter viewAdapter;
	private MyprogressDialog progressDialog;
	private ImageButton back;
	private TextView tv;
	private List<WyuNew> mdata;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int flag = msg.what;
			switch (flag) {
			case DATASUCCESS:
				initView();
				Toast.makeText(getActivity(), "数据初始化成功", Toast.LENGTH_SHORT).show();
				break;
			case DATAFAIL:
				erorDialog();
				break;
			case VIEWSUCCESS:
				changeView(1);
				initEvent();
				break;
			case VIEWFAIL:
				changeView(2);
				Toast.makeText(getActivity(), "视图初始化失败", Toast.LENGTH_SHORT).show();
				break;
			case ONLOADING_ERROR:
				Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
				break;
			case REFRESHING_ERROR:
				Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("XyggActivity", "开启了Xygg页面");
		view = inflater.inflate(R.layout.main_layout_news, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			isNew = false;
			JwPage = ((String[]) bundle.get("wyuExam"))[0];
			JwURL = ((String[]) bundle.get("wyuExam"))[1];
		} else {
			isNew = true;
		}
		progressDialog = MyprogressDialog.createDialog(getActivity());
		tv = (TextView) view.findViewById(R.id.xygg_pager_onloading);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkNetWork()) {
					initData();
					tv.setVisibility(View.GONE);
				}
			}
		});
		initData();
	}

	private boolean checkNetWork() {
		ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Service.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileInfo.isConnected() && !wifiInfo.isConnected()) {
			if (isNew) {
				Toast.makeText(getActivity(), "当前网络为非WIFI状态,可能会产生额外流量损耗", Toast.LENGTH_LONG).show();
			}
		} else if (!mobileInfo.isConnected() && !wifiInfo.isConnected()) {
			Toast.makeText(getActivity(), "无网络连接，请连接网络", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		changeView(2);
		if (isNew) {
			mdata = (List<WyuNew>) CacheUtil.getCache(getContext(), "wyuNews");
		} else {
			mdata = (List<WyuNew>) CacheUtil.getCache(getContext(), "wyuExam");
		}
		if (mdata == null) {
			if (checkNetWork()) {
				new Thread() {
					@Override
					public void run() {
						Message message = new Message();
						Log.i("XyggActivity", "加载页面");
						String html = WyuNews.getJwxx(JwPage);
						if (TextUtils.isEmpty(html)) {
							Log.i("新闻", "html为空");
						} else {

							mdata = JxHtml.jiaowu(html);
							if (!mdata.isEmpty()) {
								message.what = DATASUCCESS;
								if (isNew) {
									CacheUtil.setCache(getContext(), "wyuNews", mdata);
								} else {
									CacheUtil.setCache(getContext(), "wyuExam", mdata);
								}
							} else {
								message.what = DATAFAIL;
							}
							handler.sendMessage(message);
						}
					}
				}.start();
			} else {
				tv.setVisibility(View.VISIBLE);
				changeView(1);
			}
		} else {
			initView();
		}
	}

	private void erorDialog() {
		AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
		alert.setTitle("连接问题");
		alert.setMessage("未能加载到所需要的信息，请选择是否重新加载");
		alert.setButton(DialogInterface.BUTTON_POSITIVE, "重新加载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				initData();
			}
		});
		alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alert.show();
	}

	private void initEvent() {
		listview.setOnRefreshListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getContext(), MyWebView.class);
				intent.putExtra("Wyu", mdata.get(position - 1));
				startActivity(intent);
			}
		});

	}

	private void initView() {
		Message message = new Message();
		listview = (RefreshListView) view.findViewById(R.id.xygg_listview);
		back = (ImageButton) view.findViewById(R.id.tool_item_back);
		viewAdapter = new RefreshViewAdapter(getActivity(), mdata);
		listview.setAdapter(viewAdapter);
		if (listview != null) {
			message.what = VIEWSUCCESS;
		} else {
			message.what = VIEWFAIL;
		}
		handler.sendMessage(message);
	}

	@Override
	public void onDownRefresh() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				String html = WyuNews.getJwxx(JwPage);
				if (html != null && html.length() > 0) {
					mdata = JxHtml.jiaowu(html);
					if (isNew) {
						CacheUtil.setCache(getContext(), "wyuNews", mdata);
					} else {
						CacheUtil.setCache(getContext(), "wyuExam", mdata);
					}
				} else {
					Message message = new Message();
					message.what = REFRESHING_ERROR;
					handler.sendMessage(message);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				viewAdapter.notifyDataSetChanged();
				listview.hideHeaderView();
			}

		}.execute(new Void[] {});
	}

	@Override
	public void onLoading() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				String html = WyuNews.getJwxx(JwURL + (++page));
				if (html != null && html.length() > 0) {
					mdata.addAll(mdata.size(), JxHtml.jiaowu(html));
					if (isNew) {
						CacheUtil.setCache(getContext(), "wyuNews", mdata);
					} else {
						CacheUtil.setCache(getContext(), "wyuExam", mdata);
					}
				} else {
					Message message = new Message();
					message.what = ONLOADING_ERROR;
					handler.sendMessage(message);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				viewAdapter.notifyDataSetChanged();
				listview.hideFooterView();
			}
		}.execute(new Void[] {});

	}

	private void changeView(int flag) {
		switch (flag) {
		case 1:
			progressDialog.endDialog();
			break;
		case 2:
			progressDialog.setText("正在获取数据...");
			progressDialog.startDialog();
			break;
		default:
			break;
		}
	}

}
