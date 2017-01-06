package com.jiale.util;

import java.util.Calendar;

public class ForCurrentWeekUtil {
	public static int getCurrentWeek(SharedPreferencesHelper help) {
		long fromtime = help.getLongPreferences("weekMillsSecond");
		int currentweek = help.getIntPreferences("currentWeek");
		Calendar toC = Calendar.getInstance();
		Calendar fromC = Calendar.getInstance();
		fromC.setTimeInMillis(fromtime);
		int day = fromC.get(Calendar.DAY_OF_WEEK) - 1;
		int time = (int) ((toC.getTimeInMillis() - fromC.getTimeInMillis()) / (24 * 60 * 60 * 1000));
		if (time > 0) {
			int day2 = 7 - day;
			if (day == 0) {
				day2 = 0;
			}
			currentweek++;
			currentweek += (time - day2) / 7;

		}
		return currentweek;
	}
}
