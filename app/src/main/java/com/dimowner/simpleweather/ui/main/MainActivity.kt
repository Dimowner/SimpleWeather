/*
 *  Copyright 2018 Dmitriy Ponomarenko
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership. The ASF licenses this
 *  file to you under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package com.dimowner.simpleweather.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.dimowner.simpleweather.R
import com.dimowner.simpleweather.SWApplication
import com.dimowner.simpleweather.data.Prefs
import com.dimowner.simpleweather.ui.settings.SettingsActivity
import com.dimowner.simpleweather.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

	private val ITEM_TODAY = 0
	private val ITEM_TOMORROW = 1

	@Inject lateinit var prefs: Prefs

	private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		item.isChecked = true
		when (item.itemId) {
			R.id.nav_today -> {
				pager.setCurrentItem(ITEM_TODAY, true)
			}
			R.id.nav_tomorrow -> {
				pager.setCurrentItem(ITEM_TOMORROW, true)
			}
		}
		false
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		setTheme(R.style.AppTheme)
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setSupportActionBar(toolbar)

		SWApplication.get(applicationContext).applicationComponent().inject(this)

		if (prefs.isFirstRun()) {
			startActivity(Intent(applicationContext, WelcomeActivity::class.java))
			finish()
		} else {
			val fragments = ArrayList<Fragment>()
			fragments.add(WeatherDetailsFragment.newInstance(WeatherDetailsFragment.TYPE_TODAY))
			fragments.add(WeatherDetailsFragment.newInstance(WeatherDetailsFragment.TYPE_TOMORROW))
			val adapter = MyPagerAdapter(supportFragmentManager, fragments)
			pager.adapter = adapter
			pager.addOnPageChangeListener(this)

			bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
		}
	}

	override fun onPageScrollStateChanged(state: Int) {}
	override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

	override fun onPageSelected(position: Int) {
		val view: View
		when (position) {
			ITEM_TODAY -> {
				view = bottomNavigation.findViewById(R.id.nav_today)
				view.performClick()
			}
			ITEM_TOMORROW -> {
				view = bottomNavigation.findViewById(R.id.nav_tomorrow)
				view.performClick()
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item != null) {
//			if (item.itemId == R.id.action_locate) {
//				//Locate
//				startActivity(Intent(applicationContext, WelcomeActivity::class.java))
//			} else
			if (item.itemId == R.id.action_settings) {
				startActivity(Intent(applicationContext, SettingsActivity::class.java))
			}
		}
		return super.onOptionsItemSelected(item)
	}

	private class MyPagerAdapter internal constructor(
			fm: FragmentManager,
			private val fragments: List<Fragment>) : FragmentPagerAdapter(fm) {

		override fun getItem(position: Int): Fragment {
			return fragments[position]
		}

		override fun getCount(): Int {
			return fragments.size
		}
	}
}
