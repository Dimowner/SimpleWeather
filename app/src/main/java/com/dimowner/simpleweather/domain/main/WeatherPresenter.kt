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

package com.dimowner.simpleweather.domain.main

import android.content.Context
import com.dimowner.simpleweather.Constants
import com.dimowner.simpleweather.data.Prefs
import com.dimowner.simpleweather.data.repository.Repository
import com.dimowner.simpleweather.utils.TimeUtils
import com.dimowner.simpleweather.utils.WeatherUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class WeatherPresenter(val repository: Repository, val prefs: Prefs, val context: Context) : WeatherContract.UserActionsListener {

	var view: WeatherContract.View? = null

	var disposable: Disposable? = null

	override fun bindView(view: WeatherContract.View) {
		this.view = view
	}

	override fun unbindView() {
		this.view = null
		this.disposable?.dispose()
	}

	override fun locate() {
		//TODO: waiting for implementation
	}

	override fun updateWeather() {
		view?.showProgress()
		disposable = repository.getWeather()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({
					view?.showDate(TimeUtils.formatTime(it.dt * 1000, prefs.getTimeFormat()))
					view?.showTemperature(WeatherUtils.formatTemp(it.main.temp, prefs.getTempFormat(), context).toString())

					view?.showWind(WeatherUtils.formatWind(it.wind.speed, prefs.getWindFormat(), context))
					view?.showHumidity(WeatherUtils.formatHumidity(it.main.humidity, context))
					view?.showPressure(WeatherUtils.formatPressure(it.main.pressure, prefs.getPressureFormat(), context))

					view?.showWeatherIcon(Constants.WEATHER_ICON_URL + it.weather[0].icon + Constants.PNG)

					view?.hideProgress()
				}, {
					view?.hideProgress()
					Timber.e(it)
				})
	}
}