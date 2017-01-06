package com.jiale.util;

import java.util.Calendar;
import java.util.List;

import com.jiale.bean.Syllabus;
import com.jiale.ceter.StudentSyllabus;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadCastRecevicer extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		List<Syllabus> list = (List<Syllabus>) CacheUtil.getCache(context, "syllabus");
		StringBuffer sb = new StringBuffer();
		int currentweek = ForCurrentWeekUtil.getCurrentWeek(new SharedPreferencesHelper(context, "setting"));
		Calendar c = Calendar.getInstance();
		System.out.println("" + c.get(Calendar.DAY_OF_WEEK));
		if (list != null) {
			for (Syllabus s : list) {
				if (c.get(Calendar.DAY_OF_WEEK) == s.getDay()) {
					if ((s.getStartUP() <= currentweek && s.getEndUP() >= currentweek)
							|| (s.getStartDown() <= currentweek && s.getEndDown() >= currentweek)
							|| (s.getStartUP() == currentweek && s.getEndUP() == 0)) {
						System.out.println(s.getCourseName());
						sb.append(s.getCourseName() + "@" + s.getAddress() + "\n");
					}
				}
			}
		} else {
			sb.append("每天没有课噢");
		}
		final NotificationManager nfm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notiIntent = new Intent(context, StudentSyllabus.class);
		PendingIntent pi = PendingIntent.getActivity(context, 0, notiIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		Builder builder = new Notification.Builder(context);
		builder.setContentText(sb.toString());
		builder.setContentTitle("明日课程安排");
		builder.setContentIntent(pi);
		builder.setAutoCancel(true);
		builder.setSmallIcon(com.jiale.ceter.R.drawable.ic_launcher);
		builder.setTicker("课程提醒");
		Notification noti = builder.build();
		nfm.notify(1, noti);
	}

}
