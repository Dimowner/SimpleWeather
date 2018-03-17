/*
 * Copyright 2018 Dmitriy Ponomarenko
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership. The ASF licenses this
 * file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.dimowner.simpleweather.utils;

import android.content.Context;

import com.dimowner.simpleweather.Constants;
import com.dimowner.simpleweather.R;

public class WeatherUtils {

	public static float kelvinToCelsius(float tempKelvin) {
		return tempKelvin - 273.16f;
	}

	public static String formatTemp(float tempKelvin, int tempFormat) {
		if (tempFormat == Constants.TEMP_FORMAT_FAHRENHEIT) {
			return (int) (tempKelvin * 9/5 - 459.67) + "°F";
		} else {
			return (int) (tempKelvin - 273.16f) + "°C";
		}
	}

	public static String formatTemp(float tempKelvin, int tempFormat, Context context) {
		if (tempFormat == Constants.TEMP_FORMAT_FAHRENHEIT) {
			return (int) (tempKelvin * 9/5 - 459.67) + context.getResources().getString(R.string.temp_fahrenheit);
		} else {
			return (int) (tempKelvin - 273.16f) + context.getResources().getString(R.string.temp_celsius);
		}
	}

	public static String formatWind(float wind, int format, Context context) {
		if (format == Constants.WIND_FORMAT_MILES_PER_HOUR) {
			return (int) (wind * 2.236936) + " " + context.getResources().getString(R.string.wind_miles_hour);
		} else {
			return (int) wind + " " + context.getResources().getString(R.string.wind_meter_sec);
		}
	}

	public static String formatPressure(float pressure, int format, Context context) {
		if (format == Constants.PRESSURE_FORMAT_PHA) {
			return (int) pressure + " " + context.getResources().getString(R.string.pressure_pha);
		} else {
			return (int) (pressure / 1.33322387415) + " " + context.getResources().getString(R.string.pressure_mm_hg);
		}
	}

	public static String formatHumidity(float humidity, Context context) {
		return (int)humidity + " " + context.getResources().getString(R.string.humidity_present);
	}

}
