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
import com.vjezba.domain.model.WeatherData
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.customcontrol.RecyclerViewPaginationListener
import com.vjezba.weatherapi.ui.adapters.WeatherAdapter
import com.vjezba.weatherapi.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*


class WeatherActivity : BaseActivity(R.id.no_internet_layout) {

    val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var weatherAdapter: WeatherAdapter
    val weatherLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    private var isLastPage = false
    private var loading = false
    private var page: Int = 1


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

        initializeUi()

        weatherViewModel.weatherList.observe(this@WeatherActivity, Observer { items ->
            //Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu: ${items.result.joinToString { "-" }}")
            progressBar.visibility = View.GONE
            if( page > 1 )
                weatherAdapter.removeLoading()
            loading = false

            weatherAdapter.updateDevices(items.weatherList.toMutableList())
            //moviesAdapter.updateDevices(items.result.toMutableList())
        })

        weatherViewModel.getWeatherForeastDataFromRestApi("")
    }

    private fun initializeUi() {

        weatherAdapter = WeatherAdapter( mutableListOf<WeatherData>(),
            { movieId: Long -> setMoviesClickListener( movieId ) }  )

        weather_list.apply {
            layoutManager = weatherLayoutManager
            adapter = weatherAdapter
        }
        weather_list.adapter = weatherAdapter

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        weather_list.addOnScrollListener(object : RecyclerViewPaginationListener(weatherLayoutManager) {

            override fun loadMoreItems() {
                loading = true
                //if( connectivityUtil.isConnectedToInternet() )
                //    doRestApiCall()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return loading
            }
        })
    }

    private fun doRestApiCall() {
        weatherAdapter.addLoading()
        page++
        weatherViewModel.getWeatherForeastDataFromRestApi("")
        Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu pageNumber is: ${page}")
    }

    private fun setMoviesClickListener(movieId: Long) {
        val intent = Intent( this, MoviesDetailsActivity::class.java )
        intent.putExtra("movieId", movieId)
        startActivity(intent)
        finish()
    }

}