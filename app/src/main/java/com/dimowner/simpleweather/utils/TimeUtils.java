package com.dimowner.simpleweather.utils;

import com.dimowner.simpleweather.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

	/** Date format: May 16, 03:30 PM */
	private static SimpleDateFormat dateFormat12H = new SimpleDateFormat("MMM dd, hh:mm aa", Locale.US);

	/** Date format: May 16, 15:30 */
	private static SimpleDateFormat dateFormat24H = new SimpleDateFormat("MMM dd, HH:mm", Locale.US);

	public static final int INTERVAL_SECOND = 1000; //mills
	public static final int INTERVAL_MINUTE = 60 * INTERVAL_SECOND;
	public static final int INTERVAL_HOUR = 60 * INTERVAL_MINUTE;
	public static final int INTERVAL_DAY = 24 * INTERVAL_HOUR;

	private TimeUtils() {}

	public static String formatTimeGMT(long timeMillsGmt, int timeFormat) {
		if (timeMillsGmt <= 0) {
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		long offset = calendar.getTimeZone().getRawOffset();
		long dstSavings = calendar.getTimeZone().getDSTSavings();

		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(timeMillsGmt + offset + dstSavings);

		Calendar dayYesterday = Calendar.getInstance();
		dayYesterday.setTimeInMillis(date.getTimeInMillis());
		dayYesterday.set(Calendar.DAY_OF_YEAR, dayYesterday.get(Calendar.DAY_OF_YEAR) - 1);

		Calendar prevYear = Calendar.getInstance();
		prevYear.setTimeInMillis(date.getTimeInMillis());
		prevYear.set(Calendar.YEAR, prevYear.get(Calendar.YEAR) - 1);

		if (timeFormat == Constants.TIME_FORMAT_12H) {
			return dateFormat12H.format(new Date(timeMillsGmt + offset + dstSavings));
		} else {
			return dateFormat24H.format(new Date(timeMillsGmt + offset + dstSavings));
		}


	}

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
