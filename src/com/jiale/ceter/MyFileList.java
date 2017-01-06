package com.jiale.ceter;

import java.io.File;
import java.util.List;

import com.jiale.adapter.FileListViewAdapter;
import com.jiale.adapter.FileListViewAdapter.HideListener;
import com.jiale.bean.FileInfo;
import com.jiale.bean.FileListViewItem;
import com.jiale.util.CacheUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyFileList extends Activity implements OnClickListener {
	private ListView listview;
	private FileListViewAdapter adapter;
	private List<FileInfo> data;
	private ImageButton back;
	private Button allCheck;
	private TextView fileCount;
	private RelativeLayout tool_head;
	private RelativeLayout download_top;
	private LinearLayout tool_footer;
	private HideListener listener;
	private Button allCancel;
	private LinearLayout delete;
	private LinearLayout open;
	private TextView open_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.setting_download_listview);
		super.onCreate(savedInstanceState);
		data = CacheUtil.getDownLoadFileList(MyFileList.this);
		initView();
		initEvent();
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.setting_download_back);
		allCheck = (Button) findViewById(R.id.download_tool_allcheck);
		fileCount = (TextView) findViewById(R.id.download_tool_filecount);
		allCancel = (Button) findViewById(R.id.download_tool_allcancel);
		open = (LinearLayout) findViewById(R.id.download_tool_fileopen);
		open_text = (TextView) findViewById(R.id.footertool_fileopen_text);
		delete = (LinearLayout) findViewById(R.id.download_tool_filedelete);
		listview = (ListView) findViewById(R.id.download_listview);
		tool_head = (RelativeLayout) findViewById(R.id.download_tool_head);
		tool_footer = (LinearLayout) findViewById(R.id.download_tool_footer);
		download_top = (RelativeLayout) findViewById(R.id.download_top);
		listener = new HideListener() {

			@Override
			public void show() {
				Log.i("hidelayout", "VISIBLE");
				tool_head.setVisibility(View.VISIBLE);
				tool_footer.setVisibility(View.VISIBLE);
				download_top.setVisibility(View.GONE);
			}

			@Override
			public void hide() {
				Log.i("hidelayout", "GONE");
				tool_head.setVisibility(View.GONE);
				tool_footer.setVisibility(View.GONE);
				download_top.setVisibility(View.VISIBLE);
			}

			@Override
			public void isClick(boolean click) {
				open.setClickable(click);
				if (click) {
					open_text.setTextColor(getResources().getColor(R.color.black));
					open.setBackgroundResource(R.drawable.button);
				} else {
					open_text.setTextColor(getResources().getColor(R.color.gray));
					open.setBackgroundResource(0);
				}
			}

			@Override
			public void changeCheckNum(int num) {
				if (num > 0) {
					fileCount.setText("已选中" + num + "项");
				} else {
					fileCount.setText("");
				}
			}

		};
		adapter = new FileListViewAdapter(this, data);
		adapter.setListenser(listener);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				File file = new File(data.get(position).getPath());
				openFile(file);
			}
		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
				FileListViewItem item = (FileListViewItem) view.getTag();
				item.getCheckBox().toggle();
				adapter.getCheckList().set(position, item.getCheckBox().isChecked());
				int num = adapter.getCheckNum();
				if (num > 0) {
					fileCount.setText("已选中" + num + "项");
					if (listener != null) {
						Log.i("hidelayout", "show");
						listener.show();
					}
				} else {
					fileCount.setText("");
					if (listener != null) {
						Log.i("hidelayout", "hide");
						listener.hide();
					}
				}
				if (num == 1) {
					if (listener != null) {
						Log.i("OPNE", "TRUE");
						listener.isClick(true);
					}
				} else {
					if (listener != null) {
						Log.i("OPNE", "false");
						listener.isClick(false);
					}
				}
				return true;
			}

		});
	}

	private void initEvent() {
		back.setOnClickListener(this);
		allCheck.setOnClickListener(this);
		allCancel.setOnClickListener(this);
		open.setOnClickListener(this);
		delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_download_back:
			finish();
			break;
		case R.id.download_tool_allcheck:
			allcheck(true);
			break;
		case R.id.download_tool_allcancel:
			allcheck(false);
			break;
		case R.id.download_tool_filedelete:
			deleteFile();
			break;
		case R.id.download_tool_fileopen:
			Log.i("open", "打开");
			File file = getopenFile();
			if (file != null) {
				openFile(file);
			} else {
				Toast.makeText(MyFileList.this, "未选中打开的文件", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	private void allcheck(boolean check) {
		for (int i = 0; i < data.size(); i++) {
			adapter.getCheckList().set(i, check);
		}
		if (check) {
			if (listener != null) {
				Log.i("hidelayout", "show");
				listener.show();
			}
			fileCount.setText("已选中" + data.size() + "项");
		} else {
			if (listener != null) {
				Log.i("hidelayout", "hide");
				listener.hide();
			}
			fileCount.setText("");
		}
		if (adapter.getCheckNum() != 1) {
			open.setClickable(false);
			open_text.setTextColor(getResources().getColor(R.color.gray));
			open.setBackgroundResource(0);
		}
		adapter.notifyDataSetChanged();
	}

	private void deleteFile() {
		for (int i = 0; i < data.size(); i++) {
			if (adapter.getCheckList().get(i)) {
				CacheUtil.deleteFile(data.get(i).getPath());
				data.remove(i);
				adapter.getCheckList().remove(i);
				--i;
			}
		}
		if (listener != null) {
			listener.hide();
		}
		adapter.notifyDataSetChanged();
	}

	private File getopenFile() {
		for (int i = 0; i < data.size(); i++) {
			if (adapter.getCheckList().get(i)) {
				return new File(data.get(i).getPath());
			}
		}
		return null;
	}

	private void openFile(File file) {
		if (file.exists() && file.isFile()) {
			Intent intent = getFileIntent(file);
			final PackageManager pm = getPackageManager();
			List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
			if (list.size() > 0) {
				startActivity(intent);
				listener.hide();
			} else {
				Toast.makeText(MyFileList.this, "没有应用可打开文件，请下载应用后再尝试打开", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(MyFileList.this, "文件不存在", Toast.LENGTH_SHORT).show();
		}
		allcheck(false);
		listener.hide();
	}

	private Intent getFileIntent(File file) {
		Uri uri = Uri.fromFile(file);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, CacheUtil.getFileType(file));
		return intent;
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
