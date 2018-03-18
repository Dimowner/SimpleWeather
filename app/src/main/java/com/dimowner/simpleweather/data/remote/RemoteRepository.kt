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

package com.dimowner.simpleweather.data.remote

import com.dimowner.simpleweather.Constants
import com.dimowner.simpleweather.data.Mapper
import com.dimowner.simpleweather.data.local.room.AppDatabase
import com.dimowner.simpleweather.data.local.room.WeatherEntity
import com.dimowner.simpleweather.data.repository.Repository
import io.reactivex.Flowable
import io.reactivex.Single

class RemoteRepository (
		private val weatherApi: WeatherApi
	): Repository {

	override fun getWeatherToday(): Single<WeatherEntity> {
		return weatherApi.getWeather("Kyiv", Constants.OPEN_WEATHER_MAP_API_KEY)
				.map{ w -> Mapper.convertWeatherResponseToEntity(AppDatabase.ITEM_TYPE_TODAY, w) }
	}

	override fun getWeatherTomorrow(): Single<WeatherEntity> {
		return weatherApi.getWeather("Kyiv", Constants.OPEN_WEATHER_MAP_API_KEY)
				.map{ w -> Mapper.convertWeatherResponseToEntity(AppDatabase.ITEM_TYPE_TODAY, w) }
	}

	override fun subscribeWeatherToday(): Flowable<WeatherEntity> {
		return weatherApi.getWeather("Kyiv", Constants.OPEN_WEATHER_MAP_API_KEY).toFlowable()
				.map{ w -> Mapper.convertWeatherResponseToEntity(AppDatabase.ITEM_TYPE_TODAY, w) }
	}

	override fun subscribeWeatherTomorrow(): Flowable<WeatherEntity> {
		return weatherApi.getWeather("Kyiv", Constants.OPEN_WEATHER_MAP_API_KEY).toFlowable()
				.map{ w -> Mapper.convertWeatherResponseToEntity(AppDatabase.ITEM_TYPE_TOMORROW, w) }
	}

	override fun subscribeWeatherTwoWeeks(): Flowable<List<WeatherEntity>> {
		return weatherApi.getWeatherFewDays("Kyiv", 14, Constants.OPEN_WEATHER_MAP_API_KEY).toFlowable()
				.map{ w -> Mapper.convertWeatherListResponseToEntityList(AppDatabase.ITEM_TYPE_TWO_WEEKS, w) }
	}

	override fun cacheWeather(entity: List<WeatherEntity>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun cacheWeather(entity: WeatherEntity) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}