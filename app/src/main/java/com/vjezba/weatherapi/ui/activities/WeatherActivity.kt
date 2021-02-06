package com.vjezba.weatherapi.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*


class WeatherActivity : BaseActivity(R.id.no_internet_layout) {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    val weatherViewModel: WeatherViewModel by viewModels()

    var cityName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
    }

    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        if( viewLoaded == true )
            updateConnectivityUi()
    }

    override fun onStart() {
        super.onStart()
        viewLoaded = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        weatherViewModel.getLastLocationListener(this, fusedLocationClient)

        setupLiveDataAndObservePattern()

        setupClickListener()
    }

    private fun setupClickListener() {
        btnStartForecast.setOnClickListener {
            val intent = Intent(this, ForecastActivity::class.java)
            intent.putExtra("cityName", cityName)
            startActivity(intent)
            finish()
        }
    }

    private fun setupLiveDataAndObservePattern() {

        weatherViewModel.lastLocation.observe(this, Observer { address ->
            tvCurrentAddress.text = "Current address: " + address.getAddressLine(0)
            if( address.hasLatitude() && address.hasLongitude() )
                tvLatLongitude.text = "Latitude: " + address.latitude + " Longitude: " + address.longitude
        })

        weatherViewModel.forecastList.observe(this@WeatherActivity, Observer { item ->
            //Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu: ${items.result.joinToString { "-" }}")
            progressBar.visibility = View.GONE
            if( item.weather.isNotEmpty() ) {
                tvDescription.visibility = View.VISIBLE
                tvDescription.text = "Description: " + item.weather[0].description
            }
            else
                tvDescription.visibility = View.GONE
            tvTemp.text = "Temp: " + item.main.temp
            tvMax.text = "Temp max: " + item.main.tempMax
            tvFeelsLike.text = "Feels like: " + item.main.feelsLike
            tvWind.text = "Wind Speed: " + item.wind.speed
            cityName = item.name
        })
    }



}