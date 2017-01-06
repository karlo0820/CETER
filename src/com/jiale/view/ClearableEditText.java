package com.jiale.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.jiale.ceter.R;

public class ClearableEditText extends EditText implements OnFocusChangeListener, TextWatcher {
	private Drawable mDrawable;//删除图标
	private boolean hasFocus;

	public ClearableEditText(Context context) {
		super(context);
		init();
	}

	public ClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		//EditText有左上右下 4个 Drawable 分别为0，1，2，3
		mDrawable = getCompoundDrawables()[2];
		if (mDrawable == null) {
			mDrawable = getResources().getDrawable(R.drawable.user_delete);
		}
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
		setDrawableVisible(false);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);

	}

	private void setDrawableVisible(boolean visible) {
		Drawable right = visible ? mDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
			int eventX = (int) event.getX();
			if (eventX > (getWidth() - getTotalPaddingRight()) && eventX < getWidth() - getPaddingRight()) {
				setText("");
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFocus = hasFocus;
		if (hasFocus) {
			setDrawableVisible(getText().length() > 0);
		} else {
			setDrawableVisible(false);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		if (hasFocus) {
			setDrawableVisible(getText().length() > 0);
		}
	}

}
