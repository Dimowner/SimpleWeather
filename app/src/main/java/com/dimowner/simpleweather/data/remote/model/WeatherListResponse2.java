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

package com.dimowner.simpleweather.data.remote.model;

import java.util.Arrays;

/**
 * Created by dimowner on 3/17/18.
 */

public class WeatherListResponse2 {

	public City city;
	public String cod;
	public float message;
	public int cnt;
	public WeatherListItem[] list;

	public WeatherListResponse2(City city, String cod, float message, int cnt, WeatherListItem[] list) {
		this.city = city;
		this.cod = cod;
		this.message = message;
		this.cnt = cnt;
		this.list = list;
	}

	@Override
	public String toString() {
		return "WeatherListResponse2{" +
				"city=" + city +
				", cod='" + cod + '\'' +
				", message=" + message +
				", cnt=" + cnt +
				", list=" + Arrays.toString(list) +
				'}';
	}

	//	@SerializedName("city")
//	val city : City,
//	@SerializedName("cod")
//	val cod: String,
//	@SerializedName("message")
//	val message: Float,
//	@SerializedName("cnt")
//	val cnt: Int,
//	@SerializedName("list")
//	val list: List<WeatherListItem>
}
