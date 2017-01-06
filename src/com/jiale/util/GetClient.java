package com.jiale.util;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class GetClient {
	public static HttpClient httpClient;

	private GetClient() {
	}

	public static synchronized HttpClient getHttpClient() {
		if (null == httpClient) {
			httpClient = new DefaultHttpClient();
			Log.i("httpClient", "获得新的client");
		}
		return httpClient;
	}
}
