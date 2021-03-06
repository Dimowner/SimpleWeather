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

package com.dimowner.simpleweather.dagger.location

import com.dimowner.simpleweather.data.Prefs
import com.dimowner.simpleweather.domain.location.LocationContract
import com.dimowner.simpleweather.domain.location.LocationPresenter
import com.dimowner.simpleweather.domain.location.LocationProvider
import dagger.Module
import dagger.Provides

@Module
class LocationModule {

	@Provides
	@LocationScope
	internal fun provideLocationPresenter(locationProvider: LocationProvider, prefs: Prefs): LocationContract.UserActionsListener {
		return LocationPresenter(locationProvider, prefs)
	}
}
