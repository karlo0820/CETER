package com.jiale.view;

import com.jiale.ceter.R;
import com.jiale.ceter.R.anim;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class MyprogressDialog extends Dialog {
	private Context context;
	private static MyprogressDialog progressdialog = null;
	private TextView progressdialogText;
	private AnimationDrawable animationdrawable;

	public MyprogressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public MyprogressDialog(Context context, int themeResId) {
		super(context, themeResId);
		this.context = context;
	}

	public static MyprogressDialog createDialog(Context context) {
		progressdialog = new MyprogressDialog(context, R.style.progressdialog_style);
		progressdialog.setContentView(R.layout.progressdialog);
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return progressdialog;
	}

	public void setText(String msg) {
		progressdialogText = (TextView) progressdialog.findViewById(R.id.progressdialog_msg);
		progressdialogText.setText(msg);
	}

	public void startDialog() {
		ImageView imageview = (ImageView) progressdialog.findViewById(R.id.progressdialog);
		animationdrawable = (AnimationDrawable) imageview.getBackground();
		animationdrawable.start();
		progressdialog.show();
	}

	public void endDialog() {
		if (progressdialog != null && animationdrawable != null) {
			animationdrawable.stop();
			progressdialog.dismiss();
		}

	}

}
