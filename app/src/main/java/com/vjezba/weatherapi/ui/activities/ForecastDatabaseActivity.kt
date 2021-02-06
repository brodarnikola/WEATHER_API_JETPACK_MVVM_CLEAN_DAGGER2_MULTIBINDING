package com.vjezba.weatherapi.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.ui.adapters.WeatherAdapter
import com.vjezba.weatherapi.viewmodels.ForecastViewModel
import kotlinx.android.synthetic.main.activity_forecast.*


class ForecastDatabaseActivity : BaseActivity(R.id.no_internet_layout) {

    var cityName = ""

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var weatherAdapter: WeatherAdapter
    val weatherLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast_database)

        cityName = intent.getStringExtra("cityName") ?: ""
    }

    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        if( viewLoaded == true )
            updateConnectivityUi()
    }

    override fun onStart() {
        super.onStart()
        viewLoaded = true

        initializeUi()

        forecastViewModel.forecastList.observe(this@ForecastDatabaseActivity, Observer { items ->
            Log.d(ContentValues.TAG, "Data is: ${items.forecastList.joinToString { "-" }}")
            progressBar.visibility = View.GONE

            weatherAdapter.updateDevices(items.forecastList.toMutableList())
        })

        forecastViewModel.getWeatherFromLocalStorage()
    }

    private fun initializeUi() {

        weatherAdapter = WeatherAdapter( mutableListOf() )

        forecast_list.apply {
            layoutManager = weatherLayoutManager
            adapter = weatherAdapter
        }
        forecast_list.adapter = weatherAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent( this, ForecastActivity::class.java )
        startActivity(intent)
        finish()
    }

}