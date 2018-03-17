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

package com.dimowner.simpleweather.ui.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.dimowner.simpleweather.R
import com.dimowner.simpleweather.SWApplication
import com.dimowner.simpleweather.domain.metrics.MetricsContract
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(), MetricsContract.View  {

	@Inject lateinit var presenter : MetricsContract.UserActionsListener

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_settings)

		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		SWApplication.get(applicationContext).applicationComponent().inject(this)

		txtWind.setOnClickListener{ presenter.switchWind() }
		txtTempFormat.setOnClickListener{ presenter.switchTemperature() }
		txtPressure.setOnClickListener{ presenter.switchPressure() }
		txtTimeFormat.setOnClickListener{ presenter.switchTimeFormat() }

		presenter.bindView(this)

		btnAbout.setOnClickListener{ showAboutDialog() }
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item != null && item.itemId == android.R.id.home) {
			finish()
		}
		return super.onOptionsItemSelected(item)
	}

	private fun showAboutDialog() {
		val fm = supportFragmentManager
		val ft = fm.beginTransaction()
		val prev = fm.findFragmentByTag("dialog_about")
		if (prev != null) {
			ft.remove(prev)
		}
		ft.addToBackStack(null)
		val dialog = AboutDialog()
		dialog.show(ft, "dialog_about")
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.unbindView()
	}

	override fun showTemperatureFormat(format : String) {
		txtTempFormat.text = format
	}

	override fun showWindFormat(format : String) {
		txtWind.text = format
	}

	override fun showPressureFormat(format : String) {
		txtPressure.text = format
	}

	override fun showTimeFormat(format : String) {
		txtTimeFormat.text = format
	}

	override fun showProgress() {}
	override fun hideProgress() {}
	override fun showError(message: String) {}
	override fun showError(resId: Int) {}
}