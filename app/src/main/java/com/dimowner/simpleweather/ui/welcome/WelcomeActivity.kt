package com.dimowner.simpleweather.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dimowner.simpleweather.R
import com.dimowner.simpleweather.SWApplication
import com.dimowner.simpleweather.ui.MainActivity
import com.dimowner.simpleweather.ui.metrics.MetricsContract
import kotlinx.android.synthetic.main.activity_welcome.*
import javax.inject.Inject

class WelcomeActivity : AppCompatActivity(), MetricsContract.View {

	@Inject lateinit var presenter : WelcomePresenter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_welcome)
		SWApplication.get(applicationContext).applicationComponent().inject(this)

		txtWind.setOnClickListener{ presenter.switchWind() }
		txtTempFormat.setOnClickListener{ presenter.switchTemperature() }
		txtPressure.setOnClickListener{ presenter.switchPressure() }
		txtTimeFormat.setOnClickListener{ presenter.switchTimeFormat() }
		buttonNext.setOnClickListener {
			presenter.firstRunExecuted()
			startActivity(Intent(applicationContext, MainActivity::class.java))
			finish()
		}

		presenter.bindView(this)
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.unbindView()
	}

	override fun showTemperatureFormat(format : String) {
		txtTempFormat.text = format
	}

	override fun showWindFormat(format : String) {
		txtWind.text = format
	}

	override fun showPressureFormat(format : String) {
		txtPressure.text = format
	}

	override fun showTimeFormat(format : String) {
		txtTimeFormat.text = format
	}

	override fun showProgress() {}
	override fun hideProgress() {}
	override fun showError(message: String) {}
	override fun showError(resId: Int) {}

}
