package com.jiale.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class WyuNews {
	// 获取校园新闻页面
	public static String getJwxx(String mainurl) {
		String html = null;
		HttpClient client = GetClient.getHttpClient();
		HttpGet get = new HttpGet(mainurl);
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Accept",
				"image/gif, image/jpeg, image/pjpeg, application/x-ms-application, application/xaml+xml, application/x-ms-xbap, */*");
		get.addHeader("Connection", "Keep-Alive");
		get.addHeader("Host", "jwc.wyu.edu.cn");
		try {
			HttpResponse response = client.execute(get);
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				html = StreamTools.readInputStream(response.getEntity().getContent());
				System.out.println(html);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (get != null) {
				get.abort();
			}
			GetClient.httpClient = null;
		}

		return html;
	}
//	// 获取校园新闻页面
//	public static String getjwxx(String mainurl) {
//		String result = null;
//		HttpURLConnection conn = null;
//		URL url = null;
//		try {
//			url = new URL(mainurl);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setConnectTimeout(10000);
//			conn.setRequestProperty("User-Agent",
//					"Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
//			InputStream is = conn.getInputStream();
//			result = StreamTools.readInputStream(is);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			conn.disconnect();
//		}
//		return result;
//	}
}
