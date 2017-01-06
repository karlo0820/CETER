package com.jiale.view;

import com.jiale.ceter.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

public class MyPopupWindow extends PopupWindow implements OnTouchListener {
	private View mainView;
	private Activity activity;
	private String local;

	public MyPopupWindow(Activity activity, View contentView, View view, String local, boolean fullWidth,
			boolean wrapHeight, boolean hasAnima) {
		this.activity = activity;
		this.local = local;
		mainView = view;
		this.setContentView(contentView);
		WindowManager wm = activity.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		int r = Math.min(width, height);
		if (wrapHeight) {
			this.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		} else {
			this.setHeight(height/2);
		}
		if (fullWidth) {
			this.setWidth(LayoutParams.MATCH_PARENT);
		} else {
			this.setWidth(r * 4 / 5);
		}
		if (hasAnima) {
			this.setAnimationStyle(R.style.mPopupwindow);
		}
		this.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.popupwindow_listview));
		this.setOutsideTouchable(true);
		this.setTouchable(true);
		this.setFocusable(true);
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				setBackgroundAlpha(1.0f);
			}
		});
		this.update();
	}

	private void setBackgroundAlpha(float alpha) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = alpha;
		activity.getWindow().setAttributes(lp);

	}

	public void showPopWindow() {
		if (this.isShowing()) {
			this.dismiss();
		} else {
			switch (local) {
			case "center":
				this.showAtLocation(mainView, Gravity.CENTER, 0, 0);
				break;
			case "bottom":
				this.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);
				break;
			default:
				break;
			}
			setBackgroundAlpha(0.7f);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (this != null && this.isShowing() && (event.getAction() == MotionEvent.ACTION_OUTSIDE
				|| event.getAction() == MotionEvent.ACTION_HOVER_ENTER)) {
			this.dismiss();
			return true;
		}
		return false;
	}
}
