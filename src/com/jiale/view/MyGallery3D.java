package com.jiale.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class MyGallery3D extends Gallery {
	private Camera mCamera = new Camera();
	private int mMaxRotationAngle = 40;// 最大转动角度
	private int mMaxZoom = -20;// 最大缩放量
	private int mCoverflowCenter;// 半径值

	public MyGallery3D(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
		this.setChildrenDrawingOrderEnabled(true);
	}

	public MyGallery3D(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
		this.setChildrenDrawingOrderEnabled(true);
	}

	public MyGallery3D(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
		this.setChildrenDrawingOrderEnabled(true);
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	// 控制gallery中每个图片的旋转(重写的gallery中方法)
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();
		int rotationAngle = 0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		if (childCenter == mCoverflowCenter) {
			transformImageBitmap(child, t, 0);
		} else {
			rotationAngle = (int) (((float) (mCoverflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
			if (Math.abs(rotationAngle) > mMaxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
			}
			transformImageBitmap(child, t, rotationAngle);
		}
		// android4.0以上会引起全部childView都倾斜显示
		if (android.os.Build.VERSION.SDK_INT > 15) {
			child.invalidate();
		}
		return true;

	}

	private void transformImageBitmap(View child, Transformation t, int rotationAngle) {
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
		final int height = child.getLayoutParams().height;
		final int width = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);
		mCamera.translate(0.0f, 0.0f, 100.0f);
		if (rotation < mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
		}
		mCamera.rotateY(rotationAngle);
		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(width / 2), -(height / 2));
		imageMatrix.postTranslate((width / 2), (height / 2));
		mCamera.restore();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoverflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		} else {
			return false;
		}
	}

}
