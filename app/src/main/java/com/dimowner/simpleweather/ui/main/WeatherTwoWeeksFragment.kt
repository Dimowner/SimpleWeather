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

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dimowner.simpleweather.R
import com.dimowner.simpleweather.SWApplication
import com.dimowner.simpleweather.data.local.room.WeatherEntity
import com.dimowner.simpleweather.domain.main.WeatherContract
import kotlinx.android.synthetic.main.fragment_weather_two_weeks.*
import javax.inject.Inject

class WeatherTwoWeeksFragment : Fragment(), WeatherContract.View {

	@Inject
	lateinit var presenter : WeatherContract.UserActionsListener

	val adapter: WeatherRecyclerAdapter by lazy { WeatherRecyclerAdapter() }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_weather_two_weeks, container, false)
		SWApplication.get(view.context).applicationComponent().inject(this)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		recyclerView.setHasFixedSize(true)
		recyclerView.layoutManager = LinearLayoutManager(activity.applicationContext)
		recyclerView.adapter = adapter

		presenter.bindView(this)
		presenter.updateWeatherTwoWeeks()
	}

	override fun showTwoWeeksWeather(list: List<WeatherEntity>) {
		adapter.setData(list)
	}

	override fun showProgress() {
	}

	override fun hideProgress() {
	}

	override fun showError(message: String) {
		Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
	}

	override fun showError(resId: Int) {
		Snackbar.make(container, resId, Snackbar.LENGTH_LONG).show()
	}
	override fun showDate(date: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun showTemperature(temp: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun showWind(wind: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun showPressure(pressure: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun showHumidity(humidity: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun showWeatherIcon(url: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}