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

package com.dimowner.simpleweather.dagger.application

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.dimowner.simpleweather.data.Prefs
import com.dimowner.simpleweather.data.local.LocalRepository
import com.dimowner.simpleweather.data.local.room.AppDatabase
import com.dimowner.simpleweather.data.remote.GeocodeRestClient
import com.dimowner.simpleweather.data.remote.RemoteRepository
import com.dimowner.simpleweather.data.remote.WeatherRestClient
import com.dimowner.simpleweather.data.repository.Repository
import com.dimowner.simpleweather.data.repository.RepositoryImpl
import com.dimowner.simpleweather.domain.location.LocationProvider
import com.dimowner.simpleweather.domain.main.WeatherContract
import com.dimowner.simpleweather.domain.main.WeatherPresenter
import com.dimowner.simpleweather.domain.metrics.MetricsContract
import com.dimowner.simpleweather.domain.metrics.MetricsPresenter
import com.dimowner.simpleweather.domain.welcome.WelcomePresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
		var appContext: Context
	) {

	@Provides
	@Singleton
	internal fun provideContext(): Context {
		return appContext
	}

	@Provides
	@Singleton
	internal fun providePrefs(context: Context): Prefs {
		return Prefs(context)
	}

	@Provides
	@Singleton
	internal fun provideWeatherRestClient(): WeatherRestClient {
		return WeatherRestClient()
	}

	@Provides
	@Singleton
	internal fun provideGeocodeRestClient(): GeocodeRestClient {
		return GeocodeRestClient()
	}

	@Provides
	internal fun provideWelcomePresenter(prefs: Prefs, context: Context): WelcomePresenter {
		return WelcomePresenter(prefs, context)
	}

	@Provides
	internal fun provideMetricsPresenter(prefs: Prefs, context: Context): MetricsContract.UserActionsListener {
		return MetricsPresenter(prefs, context)
	}

	@Provides
	internal fun provideWeatherPresenter(repository: Repository, prefs: Prefs, context: Context): WeatherContract.UserActionsListener {
		return WeatherPresenter(repository, prefs, context)
	}

	@Provides
	@Singleton
	internal fun provideLocalRepository(appDatabase: AppDatabase): LocalRepository {
		return LocalRepository(appDatabase)
	}

	@Provides
	@Singleton
	internal fun provideRemoteRepository(restClient: WeatherRestClient): RemoteRepository {
		return RemoteRepository(restClient.weatherApi)
	}

	@Provides
	@Singleton
	internal fun provideRepository(localRepository: LocalRepository,
								   remoteRepository: RemoteRepository): Repository {
		return RepositoryImpl(localRepository, remoteRepository)
	}

	@Provides
	@Singleton
	internal fun provideLocationProvider(context: Context, geocodeRestClient: GeocodeRestClient): LocationProvider {
		return LocationProvider(context, geocodeRestClient.geocodeApi)
	}

	@Provides
	@Singleton
	internal fun provideAppDatabase(context: Context): AppDatabase {
		return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "weather_db")
//				.fallbackToDestructiveMigration()
				.addMigrations(MIGRATION_1_2)
				.build()
	}

	/**
	 * Migrate from:
	 * version 1 - using the SQLiteDatabase API
	 * to
	 * version 2 - using Room
	 */
	private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
		override fun migrate(database: SupportSQLiteDatabase) {
			//Migration code here
		}
	}
}
