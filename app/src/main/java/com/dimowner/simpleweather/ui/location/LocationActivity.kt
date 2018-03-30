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

package com.dimowner.simpleweather.ui.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.dimowner.simpleweather.R
import com.dimowner.simpleweather.SWApplication
import com.dimowner.simpleweather.domain.location.LocationContract
import com.dimowner.simpleweather.domain.location.LocationProvider
import com.dimowner.simpleweather.ui.main.MainActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_location.*
import timber.log.Timber
import javax.inject.Inject
import android.widget.ArrayAdapter
import android.view.MotionEvent
import java.util.*
import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import kotlin.collections.ArrayList


class LocationActivity : AppCompatActivity(), LocationContract.View {

	private val MAP_ZOOM = 12f

	val REQ_CODE_LOCATION = 303

//	@Inject lateinit var presenter : LocationContract.UserActionsListener

	@Inject lateinit var locationDataModel: LocationProvider

	val adapter : MySimpleArrayAdapter by lazy { MySimpleArrayAdapter(Collections.emptyList(), this) }

//	private val locationDataModel : LocationSource by lazy {LocationSource(applicationContext, null)}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_location)
		SWApplication.get(applicationContext).applicationComponent().inject(this)

		locationDataModel.connect(
				object : GoogleApiClient.ConnectionCallbacks {
					override fun onConnected(bundle: Bundle?) {
						Timber.v("onConnected")
					}

					override fun onConnectionSuspended(i: Int) {
						Timber.v("onConnectionSuspended")
					}
				}
		)

//		val names = arrayOf("Иван", "Марья", "Петр", "Антон", "Даша")

		list.adapter = adapter
		list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			inputCity.setText(adapter.getItem(position))
			adapter.clear()
		}

		//Disable list scrolling
		list.setOnTouchListener { v, event -> event.action == MotionEvent.ACTION_MOVE }

		btnLocate.setOnClickListener{
			if (checkLocatePermission()) {
				findLocation()
			}
		}

		mapView.onCreate(null)
		btnApply.setOnClickListener {
//			presenter.firstRunExecuted()
			startActivity(Intent(applicationContext, MainActivity::class.java))
			finish()
		}

		inputCity.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(p0: Editable?) {}
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				if (p0 != null && p0.isNotEmpty()) {
					Timber.v("onTextChanged: %s", p0.toString())
					locationDataModel.findPlace(p0.toString())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe({
								Timber.v("Predictions: %s", it.toString())
								adapter.setItems(it)
							}, {Timber.e(it)})
				} else {
					Timber.v("Empty address str")
				}

			}
		})

//		presenter.bindView(this)
	}

	private fun checkLocatePermission(): Boolean {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQ_CODE_LOCATION)
				return false
			}
		}
		return true
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		if (requestCode == REQ_CODE_LOCATION && grantResults.isNotEmpty()
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//			presenter.recordingClicked()
			findLocation()
		}
	}

	private fun findLocation() {
		locationDataModel.findLocation()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({ location ->
					mapView.getMapAsync({map ->
						Timber.v("onLocate location %s", location.toString())
						map.clear()
						val latLng = LatLng(location.lat, location.lng)
						map.addMarker(MarkerOptions().position(latLng))
						map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM))
					})
				}, {Timber.e(it)})
	}

	override fun onStart() {
		super.onStart()
		mapView.onStart()
	}

	override fun onResume() {
		super.onResume()
		mapView.onResume()
	}

	override fun onPause() {
		super.onPause()
		mapView.onPause()
	}

	override fun onStop() {
		super.onStop()
		mapView.onStop()
	}

	override fun onDestroy() {
		super.onDestroy()
//		presenter.unbindView()
		locationDataModel.disconnect()
		mapView.onDestroy()
	}

	override fun showMapMarker() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun showPredictions(temp: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun showProgress() {}
	override fun hideProgress() {}
	override fun showError(message: String) {}
	override fun showError(resId: Int) {}

	inner class MySimpleArrayAdapter(private var values: List<String>, context: Context) : ArrayAdapter<String>(context, -1, values) {

		override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
			if (convertView == null) {
				val inflater = context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
				val textView = inflater.inflate(R.layout.list_item_prediction, parent, false) as TextView
				textView.text = values[position]

				return textView
			} else{
				(convertView as TextView).text = values[position]
				return convertView
			}
		}

		override fun getCount(): Int {
			return values.size
		}

		fun setItems(items : List<String>) {
			values = items
			notifyDataSetChanged()
		}

		override fun getItem(pos : Int) : String {
			return values[pos]
		}

		override fun clear() {
			values = ArrayList()
			notifyDataSetChanged()
		}
	}
}
