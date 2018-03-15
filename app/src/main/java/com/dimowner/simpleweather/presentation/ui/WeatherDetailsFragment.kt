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

package com.dimowner.simpleweather.presentation.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dimowner.simpleweather.R
import com.dimowner.simpleweather.SWApplication
import com.dimowner.simpleweather.data.repository.Repository
import com.dimowner.simpleweather.utils.TimeUtils
import com.dimowner.simpleweather.utils.WeatherUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_weather_details.*
import timber.log.Timber
import javax.inject.Inject

class WeatherDetailsFragment : Fragment() {

	@Inject lateinit var repository: Repository

	lateinit var disposable: Disposable

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_weather_details, container, false)
		SWApplication.get(view.context).applicationComponent().inject(this)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Timber.v("getWeather")
		disposable = repository.getWeather()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({
					Timber.v("WeatherResponse $it")

					txtTemp.text = WeatherUtils.formatTemp(it.main.temp).toString()
					txtDate.text = TimeUtils.formatTime(it.dt*1000)

					txtWind.text = getString(R.string.wind_val, it.wind.speed)
					txtHumidity.text = getString(R.string.humidity_val, it.main.humidity)
					txtPressure.text = getString(R.string.pressure_val, it.main.pressure)

				},{ Timber.e(it)})
	}

	override fun onPause() {
		super.onPause()
		disposable.dispose()
	}
}
