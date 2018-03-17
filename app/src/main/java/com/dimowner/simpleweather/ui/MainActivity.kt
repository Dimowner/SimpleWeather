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

package com.dimowner.simpleweather.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import com.dimowner.simpleweather.R
import com.dimowner.simpleweather.SWApplication
import com.dimowner.simpleweather.data.Prefs
import com.dimowner.simpleweather.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

	val NAV_ITEM_TODAY = R.id.nav_today
	val NAV_ITEM_TOMORROW = R.id.nav_tomorrow
	val NAV_ITEM_TWO_WEEKS = R.id.nav_two_weeks
	val NAV_ITEM_INVALID = -1

	@Inject lateinit var prefs: Prefs

	private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		when (item.getItemId()) {
			NAV_ITEM_TODAY -> {
				startTodayWeatherFragment()
			}
			NAV_ITEM_TOMORROW -> {
				startTomorrowWeatherFragment()
			}
			NAV_ITEM_TWO_WEEKS-> {
				startTwoWeeksWeatherFragment()
			}
		}
		false
	}

	private fun startTodayWeatherFragment() {
		Timber.d("startTodayWeatherFragment")
		supportFragmentManager
				.beginTransaction()
				.replace(R.id.fragment, WeatherDetailsFragment(), "today_weather")
				.commit()
	}

	private fun startTomorrowWeatherFragment() {
		Timber.d("startTomorrowWeatherFragment")
		supportFragmentManager
				.beginTransaction()
				.replace(R.id.fragment, WeatherDetailsFragment(), "tomorrow_weather")
				.commit()
	}

	private fun startTwoWeeksWeatherFragment() {
		Timber.d("startTwoWeeksWeatherFragment")
		supportFragmentManager
				.beginTransaction()
				.replace(R.id.fragment, WeatherDetailsFragment(), "two_weeks_weather")
				.commit()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		setTheme(R.style.AppTheme)
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setSupportActionBar(toolbar)

		Timber.v("onCreate")

		SWApplication.get(applicationContext).applicationComponent().inject(this)

		Timber.v("isFirsRun = " + prefs.isFirstRun())

		if (prefs.isFirstRun()) {
			startActivity(Intent(applicationContext, WelcomeActivity::class.java))
			finish()
		} else {
			startTodayWeatherFragment()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item != null) {
			if (item.itemId == R.id.action_locate) {
				//Locate
			} else if (item.itemId == R.id.action_settings) {

			}
		}
		return super.onOptionsItemSelected(item)
	}
}
