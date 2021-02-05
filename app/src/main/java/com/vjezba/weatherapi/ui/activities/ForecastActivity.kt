package com.vjezba.weatherapi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.domain.model.ForecastData
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.ui.adapters.WeatherAdapter
import com.vjezba.weatherapi.viewmodels.ForecastViewModel
import kotlinx.android.synthetic.main.activity_forecast.*


class ForecastActivity : BaseActivity(R.id.no_internet_layout) {

    var cityName = ""

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var weatherAdapter: WeatherAdapter
    val weatherLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

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

        forecastViewModel.forecastList.observe(this@ForecastActivity, Observer { items ->
            //Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu: ${items.result.joinToString { "-" }}")
            progressBar.visibility = View.GONE

            weatherAdapter.updateDevices(items.forecastList.toMutableList())
            //moviesAdapter.updateDevices(items.result.toMutableList())
        })

        forecastViewModel.getWeatherForeastDataFromRestApi(cityName)
    }

    private fun initializeUi() {

        weatherAdapter = WeatherAdapter( mutableListOf<ForecastData>(),
            { movieId: Long -> setMoviesClickListener( movieId ) }  )

        forecast_list.apply {
            layoutManager = weatherLayoutManager
            adapter = weatherAdapter
        }
        forecast_list.adapter = weatherAdapter
    }

    private fun setMoviesClickListener(movieId: Long) {
        val intent = Intent( this, MoviesDetailsActivity::class.java )
        intent.putExtra("movieId", movieId)
        startActivity(intent)
        finish()
    }

}