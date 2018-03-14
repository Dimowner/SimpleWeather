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

package com.dimowner.simpleweather

import android.app.Application
import android.content.Context
import com.dimowner.simpleweather.dagger.application.AppComponent
import com.dimowner.simpleweather.dagger.application.AppModule
import com.dimowner.simpleweather.dagger.application.DaggerAppComponent
import timber.log.Timber

class SWApplication : Application() {

	private val appComponent: AppComponent by lazy {
		DaggerAppComponent
				.builder()
				.appModule(AppModule(this))
				.build()
	}

	override fun onCreate() {
		super.onCreate()

		appComponent.inject(this)

		if (BuildConfig.DEBUG) {
			//Timber initialization
			Timber.plant(object : Timber.DebugTree() {
				override fun createStackElementTag(element: StackTraceElement): String {
					return super.createStackElementTag(element) + ":" + element.lineNumber
				}
			})
		}
	}

	companion object {
		fun get(context: Context): SWApplication {
			return context.applicationContext as SWApplication
		}
	}

	override fun onLowMemory() {
		super.onLowMemory()
		Timber.d("onLowMemory")
	}

	override fun onTrimMemory(level: Int) {
		super.onTrimMemory(level)
		Timber.d("onTrimMemory level = " + level)
	}

	fun applicationComponent(): AppComponent {
		return appComponent
	}
}
