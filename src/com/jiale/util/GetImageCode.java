package com.jiale.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GetImageCode {
	// ����ͼ��ݵ�¼ҳ���ȡ��֤��ͼƬ
	public static Bitmap getBitmap(String imageUrl) {
		Log.i("������֤��", "���client");
		HttpClient client = GetClient.getHttpClient();
		HttpGet httprequest = new HttpGet(imageUrl);
		byte[] bytes = null;
		Bitmap bitmap = null;
		try {
			HttpResponse response = client.execute(httprequest);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				bytes = EntityUtils.toByteArray(response.getEntity());
				bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				return bitmap;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return bitmap;

	}
}
