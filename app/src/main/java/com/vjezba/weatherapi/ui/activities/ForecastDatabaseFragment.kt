package com.vjezba.weatherapi.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.ui.adapters.WeatherAdapter
import com.vjezba.weatherapi.ui.fragments.ForecastFragment
import com.vjezba.weatherapi.viewmodels.ForecastViewModel
import kotlinx.android.synthetic.main.fragment_forecast.*


class ForecastDatabaseFragment : BaseActivity(R.id.no_internet_layout) {

    var cityName = ""

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var weatherAdapter: WeatherAdapter
    val weatherLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast_database)

        cityName = intent.getStringExtra("cityName") ?: ""

//        this.setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
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

        forecastViewModel.forecastList.observe(this@ForecastDatabaseFragment, Observer { items ->
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
        val intent = Intent( this, ForecastFragment::class.java )
        intent.putExtra("cityName", cityName)
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent( this, ForecastFragment::class.java )
                intent.putExtra("cityName", cityName)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}