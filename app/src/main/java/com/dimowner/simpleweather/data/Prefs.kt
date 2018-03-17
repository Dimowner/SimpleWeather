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

package com.dimowner.simpleweather.data

import android.content.Context
import android.content.SharedPreferences
import com.dimowner.simpleweather.Constants
import kotlin.properties.Delegates

class Prefs constructor(context: Context){

	private val PREF_NAME = "com.dimowner.simpleweather.data.Prefs"

	private val PREF_KEY_IS_FIRST_RUN = "is_first_run"
	private val PREF_KEY_TEMP_FORMAT = "temp_format"
	private val PREF_KEY_WIND_FORMAT = "wind_format"
	private val PREF_KEY_PRESSURE_FORMAT = "pressure_format"
	private val PREF_KEY_TIME_FORMAT = "time_format"

	private var preferences : SharedPreferences by Delegates.notNull()

	init {
		this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
	}

	fun isFirstRun(): Boolean {
		return !preferences.contains(PREF_KEY_IS_FIRST_RUN) || preferences.getBoolean(PREF_KEY_IS_FIRST_RUN, false)
	}

	fun firstRunExecuted() {
		val editor = preferences.edit()
		editor.putBoolean(PREF_KEY_IS_FIRST_RUN, false)
		editor.apply()
	}

	fun switchTimeFormatt() : Int {
		if (preferences.getInt(PREF_KEY_TIME_FORMAT, Constants.TIME_FORMAT_24H) == Constants.TIME_FORMAT_24H) {
			preferences.edit().putInt(PREF_KEY_TIME_FORMAT, Constants.TIME_FORMAT_12H).apply()
			return Constants.TIME_FORMAT_12H
		} else {
			preferences.edit().putInt(PREF_KEY_TIME_FORMAT, Constants.TIME_FORMAT_24H).apply()
			return Constants.TIME_FORMAT_24H
		}
	}

	fun switchWindFormat() : Int {
		if (preferences.getInt(PREF_KEY_WIND_FORMAT, Constants.WIND_FORMAT_METER_PER_HOUR) == Constants.WIND_FORMAT_METER_PER_HOUR) {
			preferences.edit().putInt(PREF_KEY_WIND_FORMAT, Constants.WIND_FORMAT_MILES_PER_HOUR).apply()
			return Constants.WIND_FORMAT_MILES_PER_HOUR
		} else {
			preferences.edit().putInt(PREF_KEY_WIND_FORMAT, Constants.WIND_FORMAT_METER_PER_HOUR).apply()
			return Constants.WIND_FORMAT_METER_PER_HOUR
		}
	}

	fun switchPressureFormat() : Int {
		if (preferences.getInt(PREF_KEY_PRESSURE_FORMAT, Constants.PRESSURE_FORMAT_MM_HG) == Constants.PRESSURE_FORMAT_MM_HG) {
			preferences.edit().putInt(PREF_KEY_PRESSURE_FORMAT, Constants.PRESSURE_FORMAT_PHA).apply()
			return Constants.PRESSURE_FORMAT_PHA
		} else {
			preferences.edit().putInt(PREF_KEY_PRESSURE_FORMAT, Constants.PRESSURE_FORMAT_MM_HG).apply()
			return Constants.PRESSURE_FORMAT_MM_HG
		}
	}

	fun switchTempFormat() : Int {
		if (preferences.getInt(PREF_KEY_TEMP_FORMAT, Constants.TEMP_FORMAT_CELSIUS) == Constants.TEMP_FORMAT_CELSIUS) {
			preferences.edit().putInt(PREF_KEY_TEMP_FORMAT, Constants.TEMP_FORMAT_FAHRENHEIT).apply()
			return Constants.TEMP_FORMAT_FAHRENHEIT
		} else {
			preferences.edit().putInt(PREF_KEY_TEMP_FORMAT, Constants.TEMP_FORMAT_CELSIUS).apply()
			return Constants.TEMP_FORMAT_CELSIUS
		}
	}

	fun getTempFormat(): Int {
		return preferences.getInt(PREF_KEY_TEMP_FORMAT, Constants.TEMP_FORMAT_CELSIUS)
	}

	fun getWindFormat(): Int {
		return preferences.getInt(PREF_KEY_WIND_FORMAT, Constants.WIND_FORMAT_METER_PER_HOUR)
	}

	fun getPressureFormat(): Int {
		return preferences.getInt(PREF_KEY_PRESSURE_FORMAT, Constants.PRESSURE_FORMAT_MM_HG)
	}

	fun getTimeFormat(): Int {
		return preferences.getInt(PREF_KEY_TIME_FORMAT, Constants.TIME_FORMAT_24H)
	}

}
