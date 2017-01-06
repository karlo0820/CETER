package com.jiale.ceter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jiale.adapter.PopupwindowAdapter;
import com.jiale.bean.MyAccount;
import com.jiale.util.CacheUtil;
import com.jiale.view.CircleImageView;
import com.jiale.view.MyPopupWindow;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingFragment extends Fragment implements OnClickListener {
	private final static int GALLERY_CODE = 1;
	private final static int CAMERA_CODE = 2;
	private final static int IMAGE_CODE = 3;
	private View view;
	private RelativeLayout setting_Set;
	private RelativeLayout setting_Cache;
	private RelativeLayout setting_Exit;
	private RelativeLayout setting_About;
	private RelativeLayout setting_DownLoad;
	private CircleImageView photo;
	private TextView accountBind;
	private TextView cache;
	private AlertDialog dialog;
	private MyPopupWindow popWindow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_layout_setting, container, false);
		Log.i("SettingActivity", "开启setting页面");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i("SettingFragment", "CreaterActivity");
		initView();
		initListener();
		super.onActivityCreated(savedInstanceState);
	}

	private void initView() {
		setting_Set = (RelativeLayout) view.findViewById(R.id.setting_item_set);
		setting_Cache = (RelativeLayout) view.findViewById(R.id.setting_item_cache);
		setting_Exit = (RelativeLayout) view.findViewById(R.id.setting_item_exit);
		setting_About = (RelativeLayout) view.findViewById(R.id.setting_item_about);
		setting_DownLoad = (RelativeLayout) view.findViewById(R.id.setting_item_download);
		cache = (TextView) view.findViewById(R.id.setting_cache_text);
		accountBind = (TextView) view.findViewById(R.id.setting_account);
		photo = (CircleImageView) view.findViewById(R.id.setting_account_icon);
		initData();

	}

	private void initListener() {
		setting_Set.setOnClickListener(this);
		setting_Cache.setOnClickListener(this);
		setting_Exit.setOnClickListener(this);
		setting_About.setOnClickListener(this);
		setting_DownLoad.setOnClickListener(this);
		photo.setOnClickListener(this);
	}

	private void initData() {
		cache.setText(CacheUtil.getFileDirectoryStorage(getContext()));
		MyAccount ma = (MyAccount) CacheUtil.getCache(getContext(), "account");
		if (ma != null) {
			accountBind.setText("当前账号：" + ma.getAccount());
			File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");
			if (file.exists()) {
				photo.setImageURI(Uri.fromFile(file));
			}
		} else {
			accountBind.setText("未绑定账号");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_account_icon:
			showWindow();
			break;
		case R.id.setting_item_about:
			Intent intent = new Intent(getActivity(), AboutApp.class);
			startActivity(intent);
			break;
		case R.id.setting_item_set:
			Log.i("settingSet", "设置");
			Intent setIntent = new Intent(getActivity(), SettingInfo.class);
			startActivity(setIntent);
			break;
		case R.id.setting_item_cache:
			showDialog();
			break;
		case R.id.setting_item_download:
			Intent fileIntent = new Intent(getActivity(), MyFileList.class);
			startActivity(fileIntent);
			break;
		case R.id.setting_item_exit:
			getActivity().finish();
			break;
		}
	}

	private void showWindow() {
		if (popWindow == null) {
			ListView listview = new ListView(getContext());
			List<String> arr = new ArrayList<String>(3);
			arr.add("拍照");
			arr.add("从手机相册选择");
			arr.add("取消");
			PopupwindowAdapter adapter = new PopupwindowAdapter(getContext(), arr);
			listview.setAdapter(adapter);
			listview.setLayoutParams(
					new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
			listview.setSelector(R.drawable.listview);
			listview.setFocusable(true);
			listview.setItemsCanFocus(true);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					System.out.println(popWindow.isShowing());
					switch (position) {
					case 0:
						chooseFromCamera();
						popWindow.showPopWindow();
						break;
					case 1:
						chooseFromGarrery();
						popWindow.showPopWindow();
						break;
					case 2:
						popWindow.showPopWindow();
						break;
					default:
						break;
					}
				}
			});
			popWindow = new MyPopupWindow(getActivity(), listview, view, "bottom", true, true, true);
		}
		popWindow.showPopWindow();
	}

	private void chooseFromGarrery() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, GALLERY_CODE);
	}

	private void chooseFromCamera() {
		if (CacheUtil.hasSDcard()) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg")));
			startActivityForResult(intent, CAMERA_CODE);
		} else {
			Toast.makeText(getActivity(), "无SD卡", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("resultCode", "" + resultCode);
		if (resultCode == 0) {
			return;
		}
		switch (requestCode) {
		case GALLERY_CODE:
			cropRawPhoto(data.getData());
			break;
		case CAMERA_CODE:
			File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");
			cropRawPhoto(Uri.fromFile(file));
			break;
		case IMAGE_CODE:
			if (data != null) {
				setImageView(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showDialog() {
		if (dialog == null) {
			Log.i("settingCachce", "清除缓存");
			dialog = new AlertDialog.Builder(getContext()).create();
			dialog.show();
			View view = LayoutInflater.from(getContext()).inflate(R.layout.setting_cache_dialog, null);
			TextView content = (TextView) view.findViewById(R.id.dialog_message);
			Button clear = (Button) view.findViewById(R.id.dialog_clear);
			Button cancel = (Button) view.findViewById(R.id.dialog_cancel);
			content.setText("确认清除应用缓存？");
			clear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CacheUtil.ClearDirectoryStorage(getContext());
					initData();
					dialog.dismiss();
				}
			});
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			WindowManager wm = dialog.getWindow().getWindowManager();
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			params.width = Math.min(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight()) * 2 / 3;
			params.height = params.WRAP_CONTENT;
			dialog.getWindow().setAttributes(params);
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(true);
		} else {
			dialog.show();
		}
	}

	private void cropRawPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, IMAGE_CODE);
	}

	private void setImageView(Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Bitmap bitmap = bundle.getParcelable("data");
			photo.setImageBitmap(bitmap);
			File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void onPause() {
		Log.i("SETTING", "PASUE");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.i("SETTING", "PASUE");
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.i("SETTING", "SAVE");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		Log.i("SETTING", "START");
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.i("SETTING", "STOP");
		super.onStop();
	}
}
