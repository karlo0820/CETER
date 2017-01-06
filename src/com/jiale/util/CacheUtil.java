package com.jiale.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jiale.bean.FileInfo;

import android.content.Context;
import android.os.Environment;
import android.provider.ContactsContract.Directory;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class CacheUtil {

	public static  boolean hasSDcard(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		return false;
	}

	public static void setCache(Context context, String url, Object object) {
		File dir = context.getFilesDir();
		File file = null;
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			file = new File(dir, url);
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static Object getCache(Context context, String url) {
		File dir = context.getFilesDir();
		File file = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			file = new File(dir, url);
			if (file.exists() && file.isFile()) {
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				return ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static boolean isFileExist(Context context, String url) {

		File dir = context.getFilesDir();
		File file = null;
		file = new File(dir, url);
		if (file.exists() && file.isFile()) {
			return true;
		}
		return false;
	}

	// 获取文件夹的大小
	public static String getFileDirectoryStorage(Context context) {
		File dir = context.getFilesDir();
		String result = null;
		if (dir.exists()) {
			File[] flist = dir.listFiles();
			long space = 0l;
			for (File file : flist) {
				file.length();
				space += file.length();
			}
			Log.i("space", "" + space);
			DecimalFormat df = new DecimalFormat("#.00");
			if (space < 1024) {
				result = df.format((double) space) + "B";
			} else if (space < 1048576) {
				result = df.format((double) space / 1024) + "KB";
			} else if (space < 1073741824) {
				result = df.format((double) space / 1048576) + "MB";
			} else {
				result = df.format((double) space / 1073741824) + "GB";
			}
		}
		return result;
	}

	// 获取文件的大小
	public static String getFileStorage(File file) {
		String result = null;
		if (file.exists() && file.isFile()) {
			long space = file.length();
			DecimalFormat df = new DecimalFormat("#.00");
			if (space < 1024) {
				result = df.format((double) space) + "B";
			} else if (space < 1048576) {
				result = df.format((double) space / 1024) + "KB";
			} else if (space < 1073741824) {
				result = df.format((double) space / 1048576) + "MB";
			} else {
				result = df.format((double) space / 1073741824) + "GB";
			}
		}
		return result;
	}

	// 删除文件夹下的文件
	public static void ClearDirectoryStorage(Context context) {
		File dir = context.getFilesDir();
		File[] fList = dir.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				file.delete();
			}
		}

	}

	public static boolean getFileUpdateTime(Context context, String url) {
		File dir = context.getFilesDir();
		File file = null;
		file = new File(dir, url);
		if (file.exists() && file.isFile()) {
			long time = (System.currentTimeMillis() - file.lastModified()) / (3600000 * 24);
			Log.i("time", time + "");
			if (time > 1) {
				return true;
			}
		}
		return false;
	}

	// 获取文件创建时间 格式yyyy-MM-dd HH:mm:ss
	public static String getFileCreateTime(File file) {
		String createTime = null;
		if (file.exists() && file.isFile()) {
			long time = file.lastModified();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(time);
			createTime = sdf.format(date);
		}
		return createTime;
	}

	// 获取文件列表
	public static List<FileInfo> getDownLoadFileList(Context context) {
		File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
		// File directory = new File(dir, "com.jiale.ceter");
		List<FileInfo> files = new ArrayList<FileInfo>();
		if (dir.isDirectory()) {
			File[] fileList = dir.listFiles();
			for (File file : fileList) {
				if (file.isFile()) {
					FileInfo info = new FileInfo();
					info.setCreateTime(getFileCreateTime(file));
					info.setName(file.getName());
					info.setSize(getFileStorage(file));
					info.setPath(file.getAbsolutePath());
					files.add(info);
				}
			}
		}
		return files;
	}

	public static String getFileType(File file) {
		String fileName = file.getName();
		String extension = fileName.substring(fileName.indexOf(".") + 1);
		Log.i("WEBVIEW---extension", extension);
		if (MimeTypeMap.getSingleton().hasExtension(extension)) {
			Log.i("WEBVIEW---MimeType", MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
			return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}
		return null;
	}

	// 删除文件
	public static void deleteFile(String url) {
		File file = new File(url);
		if (file.exists() && file.isFile()) {
			Log.i("deleteFile-->", url);
			file.delete();
		}
	}
}
