package com.jiale.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleImageView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		int width = getWidth();
		int height = getHeight();
		int r = Math.min(width, height);
		float scale = width * 1.0f / r;
		drawable.setBounds(0, 0, (int) (scale * width), (int) (scale * height));
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		canvas.drawBitmap(getCircleBitmap(bitmap), 0, 0, null);

	}

	private Bitmap getCircleBitmap(Bitmap bitmap) {
		int width = getWidth();
		int height = getHeight();
		int r = Math.min(width, height);
		Bitmap bmp = bitmap.createScaledBitmap(bitmap, r, r, false);
		// float scale = width * 1.0f / r;
		Bitmap roundBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Config.ARGB_8888);
		Paint paint = new Paint();
		//��һ����λͼͼƬ��С��Ϊ�뾶��Բ
		Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		//����һ���ڱ���λͼ��canvas����
		Canvas mCanvas = new Canvas(roundBmp);
		mCanvas.drawARGB(0, 0, 0, 0);
		//�ڱ���λͼ��һ����Rectһ����Բ
		mCanvas.drawCircle(bmp.getWidth() / 2, bmp.getHeight() / 2, bmp.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		//��λͼͼƬ��Rect�����ཻ���֣���������λͼ��Բ���С��õ�����һ��Բ�ε�λͼͼƬ��
		mCanvas.drawBitmap(bmp, rect, rect, paint);
		return roundBmp;
	}

	// public static Bitmap drawCircleBitmap(Bitmap bitmap) {
	// int width = bitmap.getWidth();
	// int height = bitmap.getHeight();
	// int r = Math.min(width, height);
	// Bitmap backgroundBitmap = Bitmap.createBitmap(width, height,
	// Config.ARGB_8888);
	// Canvas canvas = new Canvas(backgroundBitmap);
	// final Paint paint = new Paint();
	// paint.setAntiAlias(true);
	// final RectF rectf = new RectF(0, 0, r, r);
	// canvas.drawRoundRect(rectf, r / 2, r / 2, paint);
	// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	// canvas.drawBitmap(bitmap, null, rectf, paint);
	// return backgroundBitmap;
	// }
}
