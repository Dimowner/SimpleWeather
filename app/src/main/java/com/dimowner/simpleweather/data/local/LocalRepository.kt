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

package com.dimowner.simpleweather.data.local

import com.dimowner.simpleweather.data.local.room.AppDatabase
import com.dimowner.simpleweather.data.local.room.WeatherEntity
import com.dimowner.simpleweather.data.repository.Repository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LocalRepository(private val appDatabase: AppDatabase) : Repository {

	override fun subscribeWeatherToday(): Flowable<WeatherEntity> {
		return appDatabase.weatherDao().subscribeWeatherToday()
	}

	override fun subscribeWeatherTomorrow(): Flowable<WeatherEntity> {
		return appDatabase.weatherDao().subscribeWeatherTomorrow()
	}

	override fun subscribeWeatherTwoWeeks(): Flowable<List<WeatherEntity>> {
		return appDatabase.weatherDao().subscribeWeatherTwoWeeks()
	}

	override fun cacheWeather(entity: List<WeatherEntity>) {
		Single.just(entity).doOnSuccess{
			if (it.isNotEmpty()) {
				appDatabase.weatherDao().delete(it[0].type)
				appDatabase.weatherDao().insertAll(*it.toTypedArray())
			}
		}.observeOn(Schedulers.io()).subscribe({ Timber.v(it.toString())}, Timber::e)
	}

	override fun cacheWeather(entity: WeatherEntity) {
		Single.just(entity).doOnSuccess{
			appDatabase.weatherDao().delete(it.type)
			appDatabase.weatherDao().insertWeather(it)
		}.observeOn(Schedulers.io()).subscribe({Timber.v(it.toString())}, Timber::e)
	}
}