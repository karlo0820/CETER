package com.jiale.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

public class SyllabusAlarm {

	public static void openAlarm(Context context) {
		SharedPreferencesHelper helper = new SharedPreferencesHelper(context, "setting");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, helper.getIntPreferences("timePicker_hour"));
		c.set(Calendar.MINUTE, helper.getIntPreferences("timePicker_minute"));
		System.out.println(c.getTimeInMillis() + "");
		Intent intent = new Intent(context, AlarmBroadCastRecevicer.class);
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60000, pi);
	}

	public static void cancelAlarm(Context context) {
		Intent intent = new Intent(context, AlarmBroadCastRecevicer.class);
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
		am.cancel(pi);
	}
}
