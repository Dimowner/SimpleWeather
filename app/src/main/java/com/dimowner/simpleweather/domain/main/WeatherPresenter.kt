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
import com.dimowner.simpleweather.data.local.room.WeatherEntity
import com.dimowner.simpleweather.data.repository.Repository
import com.dimowner.simpleweather.ui.main.WeatherDetailsFragment
import com.dimowner.simpleweather.utils.TimeUtils
import com.dimowner.simpleweather.utils.WeatherUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class WeatherPresenter(
		private val repository: Repository,
		private val prefs: Prefs,
		private val context: Context) : WeatherContract.UserActionsListener {

	private var view: WeatherContract.View? = null

	private val disposable: CompositeDisposable by lazy { CompositeDisposable() }

	override fun bindView(view: WeatherContract.View) {
		this.view = view
	}

	override fun unbindView() {
		this.view = null
		this.disposable.dispose()
	}

	override fun locate() {
		//TODO: waiting for implementation
	}

	override fun updateWeatherTwoWeeks() {
		view?.showProgress()
		disposable.add(repository.subscribeWeatherTwoWeeks(prefs.getCity())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({d ->
					view?.showTwoWeeksWeather(d)
					view?.hideProgress()
				}, {
					view?.hideProgress()
					Timber.e(it)
					view?.showError(it.message!!)
				}))
	}

	override fun updateWeather(type: Int) {
		view?.showProgress()
		if (type == WeatherDetailsFragment.TYPE_TODAY) {
			disposable.add(repository.subscribeWeatherToday(prefs.getCity())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe({
						showData(it)
						view?.hideProgress()
					}, {
						view?.hideProgress()
						Timber.e(it)
						view?.showError(it.message!!)
					}))
		} else if (type == WeatherDetailsFragment.TYPE_TOMORROW) {
			disposable.add(repository.subscribeWeatherTomorrow(prefs.getCity())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe({
						showData(it)
						view?.hideProgress()
					}, {
						view?.hideProgress()
						Timber.e(it)
						view?.showError(it.message!!)
					}))
		}
	}

	private fun showData(entity: WeatherEntity) {
		view?.showDate(TimeUtils.formatTime(entity.dt * 1000, prefs.getTimeFormat()))
		view?.showTemperature(WeatherUtils.formatTemp(entity.temp, prefs.getTempFormat(), context).toString())

		view?.showWind(WeatherUtils.formatWind(entity.wind, prefs.getWindFormat(), context))
		view?.showHumidity(WeatherUtils.formatHumidity(entity.humidity, context))
		view?.showPressure(WeatherUtils.formatPressure(entity.pressure, prefs.getPressureFormat(), context))

		view?.showWeatherIcon(Constants.WEATHER_ICON_URL + entity.icon + Constants.PNG)
	}
}