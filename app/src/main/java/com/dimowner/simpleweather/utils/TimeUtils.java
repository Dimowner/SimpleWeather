package com.dimowner.simpleweather.utils;

import com.dimowner.simpleweather.Constants;

import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

	/** Date format: May 16, 03:30 PM */
	private static SimpleDateFormat dateFormat12H = new SimpleDateFormat("MMM dd, hh:mm aa", Locale.US);

	/** Date format: May 16, 15:30 */
	private static SimpleDateFormat dateFormat24H = new SimpleDateFormat("MMM dd, HH:mm", Locale.US);

	private TimeUtils() {}

	public static String formatTime(long timeMills, int timeFormat) {
		if (timeMills <= 0) {
			return "";
		}
		if (timeFormat == Constants.TIME_FORMAT_12H) {
			return dateFormat12H.format(new Date(timeMills));
		} else {
			return dateFormat24H.format(new Date(timeMills));
		}
	}
}
