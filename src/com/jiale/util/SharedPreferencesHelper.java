package com.jiale.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {
	private SharedPreferences preference;

	public SharedPreferencesHelper(Context context, String fileName) {
		if (fileName.equals("")) {
			return;
		}
		preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	public boolean saveBooleanPreferences(String key, boolean value) {
		Editor editor = preference.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public boolean saveIntPreferences(String key, int value) {
		Editor editor = preference.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public boolean saveLongPreferences(String key, Long value) {
		Editor editor = preference.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public boolean saveStringPreferences(String key, String value) {
		Editor editor = preference.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public String getStringPreferences(String key) {
		return preference.getString(key, "");
	}

	public boolean getBooleanPreferences(String key) {
		return preference.getBoolean(key, false);
	}

	public long getLongPreferences(String key) {
		return preference.getLong(key, System.currentTimeMillis());
	}

	public int getIntPreferences(String key) {
		return preference.getInt(key, 1);
	}

	public String getStringPreferences(String key, String defValue) {
		return preference.getString(key, defValue);
	}
}
