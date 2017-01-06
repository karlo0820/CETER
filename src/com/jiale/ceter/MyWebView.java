package com.jiale.ceter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.jiale.bean.WyuNew;
import com.jiale.util.GetClient;
import com.jiale.view.MyprogressDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyWebView extends Activity {
	private ImageButton back;
	private ProgressBar progress;
	private WebView mWebView;
	private TextView title;
	private WyuNew mNew;
	private String fundationHtml;
	private String questionHtml;
	private MyprogressDialog dialog;
	private long dataSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.webview);
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mNew = (WyuNew) intent.getSerializableExtra("Wyu");
		if (mNew == null) {
			fundationHtml = intent.getStringExtra("fundation");
			if (fundationHtml == null) {
				questionHtml = intent.getStringExtra("question");
			}
		}
		initView();
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.webviewlayout_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		progress = (ProgressBar) findViewById(R.id.webview_progress);
		progress.setMax(100);
		mWebView = (WebView) findViewById(R.id.webview);
		title = (TextView) findViewById(R.id.webviewlayout_title);
		if (mNew == null) {
			if (fundationHtml != null) {
				title.setText("功能介绍");
				mWebView.loadUrl(fundationHtml);
			}
			if (questionHtml != null) {
				title.setText("问题帮助");
				mWebView.loadUrl(questionHtml);
			}
		} else {
			title.setText(mNew.getPublicAuthor());
			mWebView.loadUrl(mNew.getHref());
		}
		WebSettings setting = mWebView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setDisplayZoomControls(false);
		setting.setBuiltInZoomControls(true);
		setting.setSupportZoom(true);
		setting.setUseWideViewPort(true);
		setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		setting.setLoadWithOverviewMode(true);
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progress.setProgress(newProgress);
				if (newProgress == 100) {
					progress.setVisibility(View.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}

		});
		mWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				Log.i("WEBVIEW", "url=" + url + ",userAgent=" + userAgent + ",contentDisposition=" + contentDisposition
						+ ",mimetype=" + mimetype + ",contentLength=" + contentLength);
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					Log.i("WEBVIEW---SD卡", "没有SD卡");
					return;
				}
				dataSize = contentLength;
				DownLoadTask download = new DownLoadTask();
				download.execute(url);
			}
		});
	}

	private class DownLoadTask extends AsyncTask<String, Long, String> {

		@Override
		protected void onProgressUpdate(Long... values) {
			super.onProgressUpdate(values);
			dialog.setText("正在下载文件:" + getDataSize(values[0]) + "/" + getDataSize(dataSize));
		}

		@Override
		protected String doInBackground(String... params) {
			Log.i("WEBVIEW----TASK", "开始下载");
			String url = params[0];
			String fileName = url.substring(url.lastIndexOf("/") + 1);
			try {
				fileName = URLDecoder.decode(fileName, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			File dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
			// File dir2 = new File(dir.getAbsolutePath(),"com.jiale.ceter");
			// if(!dir2.exists()){
			// dir2.mkdirs();
			// }
			Log.i("WEBVIEW---DIRPATH", dir.getAbsolutePath());
			// Log.i("WEBVIEW---DIR2PATH", dir2.getAbsolutePath());
			File file = new File(dir, fileName);
			Log.i("WEBVIEW----filepath", file.getAbsolutePath());
			if (file.exists()) {
				return fileName;
			}
			FileOutputStream fos = null;
			InputStream is = null;
			HttpGet get = null;
			HttpClient client = null;
			try {
				client = GetClient.getHttpClient();
				get = new HttpGet(url);
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Log.i("WEBVIEW---SD卡", "正在写入SD卡");
					fos = new FileOutputStream(file);
					is = response.getEntity().getContent();
					byte[] bytes = new byte[128];
					int len = 0;
					long result = 0;
					while ((len = is.read(bytes)) != -1) {
						fos.write(bytes, 0, len);
						fos.flush();
						result += len;
						publishProgress(result);
					}
					fos.close();
					is.close();
					return fileName;
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (get != null) {
					get.abort();
				}
				if (client != null) {
					client = null;
				}
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.endDialog();
			if (result == null) {
				Toast.makeText(MyWebView.this, "文件下载失败", Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(MyWebView.this, "文件下载成功，已添加到我的文件里", Toast.LENGTH_SHORT).show();
			Log.i("WEBVIEW---RESULT", "已经保存到SD卡");
		}

		@Override
		protected void onPreExecute() {
			dialog = MyprogressDialog.createDialog(MyWebView.this);
			dialog.setText("正在下载文件:....");
			dialog.startDialog();
			super.onPreExecute();
		}

	}

	private String getDataSize(long space) {
		String result = null;
		DecimalFormat df = new DecimalFormat("#.0");
		if (space < 1024) {
			result = df.format((double) space) + "B";
		} else if (space < 1048576) {
			result = df.format((double) space / 1024) + "KB";
		} else if (space < 1073741824) {
			result = df.format((double) space / 1048576) + "MB";
		} else {
			result = df.format((double) space / 1073741824) + "GB";
		}
		return result;
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
