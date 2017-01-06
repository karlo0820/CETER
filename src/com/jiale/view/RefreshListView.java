package com.jiale.view;

import java.text.SimpleDateFormat;

import com.jiale.ceter.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView implements OnScrollListener {
	private static final String TAG = "RefreshListView";

	private int firstitemposition; // 显示在第一个item的索引
	private int pressedY; // 按下时 Y轴的偏移量

	private static final int PULL = 1; // 下拉状态
	private static final int RELEASE = 2;// 松开状态
	private static final int REFRESHING = 3;// 正在刷新状态
	private int currentState = PULL; // 当前状态

	private Animation upAnimation;
	private Animation downAnimation;

	private View headerView; // 头布局
	private int hViewHeight; // 头布局高度
	private ImageView header_arrow;
	private ProgressBar header_pb;
	private TextView header_title;
	private TextView header_time;

	private OnRefreshListener orl;
	private boolean isBottom; // 是否到达底部
	private View footerView;
	private int fViewHeight;
	private boolean isLoad = false; // 是否在加载

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
		this.setOnScrollListener(this);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
		this.setOnScrollListener(this);
	}

	public RefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		this.setOnScrollListener(this);
	}

	private String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(System.currentTimeMillis());
	}

	private void init() {
		// 头布局
		headerView = View.inflate(getContext(), R.layout.refreshlistview_header, null);
		
		header_pb = (ProgressBar) headerView.findViewById(R.id.listview_header_pb);
		header_title = (TextView) headerView.findViewById(R.id.listview_header_title);
		header_time = (TextView) headerView.findViewById(R.id.listview_header_time);
		header_arrow = (ImageView) headerView.findViewById(R.id.listview_header_arrow);
		// header_time.setText(getCurrentTime());
		headerView.measure(0, 0);
		hViewHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -hViewHeight, 0, 0);
		this.addHeaderView(headerView);

		// 脚布局
		footerView = View.inflate(getContext(), R.layout.refreshlistview_footer, null);
		footerView.measure(0, 0);
		fViewHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -fViewHeight, 0, 0);
		this.addFooterView(footerView);
		initAnimation();
	}

	private void initAnimation() {
		upAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		upAnimation.setDuration(500);
		upAnimation.setFillAfter(true); // 动画结束停留在结束位置
		downAnimation = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		downAnimation.setDuration(500);
		downAnimation.setFillAfter(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			pressedY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveY = (int) ev.getY();
			int diff = (moveY - pressedY) / 2; // 注意
			int paddingTop = -hViewHeight + diff;
			if (firstitemposition == 0 && -hViewHeight < paddingTop) {
				if (paddingTop > 0 && currentState == PULL) {
					Log.i(TAG, "松开刷新");
					currentState = RELEASE;
					// 刷新头布局
					refreshHeaderView();
				} else if (paddingTop < 0 && currentState == RELEASE) {
					Log.i(TAG, "下拉刷新");
					currentState = PULL;
					// 刷新头布局
					refreshHeaderView();
				}
				headerView.setPadding(0, paddingTop, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (currentState == RELEASE) {
				Log.i(TAG, "刷新数据");
				headerView.setPadding(0, 0, 0, 0);
				currentState = REFRESHING;
				// 刷新头布局
				refreshHeaderView();
				if (orl != null) {
					orl.onDownRefresh();
				}

			} else if (currentState == PULL) {
				headerView.setPadding(0, -hViewHeight, 0, 0);
			}
			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	public void refreshHeaderView() {
		switch (currentState) {
		case PULL:
			header_title.setText("下拉刷新");
			header_arrow.startAnimation(downAnimation);
			break;
		case RELEASE:
			header_title.setText("松开刷新");
			header_arrow.startAnimation(upAnimation);
			break;
		case REFRESHING:
			header_arrow.clearAnimation();
			header_arrow.setVisibility(View.GONE);
			header_pb.setVisibility(View.VISIBLE);
			header_title.setText("正在拼命刷新中");
			break;
		default:
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
			if (isBottom && !isLoad) {
				isLoad = true;
				footerView.setPadding(0, 0, 0, 0);
				this.setSelection(this.getCount());
				if (orl != null) {
					orl.onLoading();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		firstitemposition = firstVisibleItem;
		if (getLastVisiblePosition() == (totalItemCount - 1)) {
			isBottom = true;
		} else {
			isBottom = false;
		}
	}

	public void setOnRefreshListener(OnRefreshListener onrefreshlistener) {
		orl = onrefreshlistener;
	}

	public void hideHeaderView() {
		headerView.setPadding(0, -hViewHeight, 0, 0);
		header_arrow.setVisibility(View.VISIBLE);
		header_pb.setVisibility(View.GONE);
		header_title.setText("下拉刷新");
		header_time.setText(getCurrentTime());
		currentState = PULL;

	}

	public void hideFooterView() {
		footerView.setPadding(0, -fViewHeight, 0, 0);
		isLoad = false;
	}
}
