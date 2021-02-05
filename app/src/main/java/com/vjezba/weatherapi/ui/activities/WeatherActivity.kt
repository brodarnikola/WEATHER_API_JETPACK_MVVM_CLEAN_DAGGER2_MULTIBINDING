package com.vjezba.weatherapi.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.vjezba.domain.model.WeatherData
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.customcontrol.RecyclerViewPaginationListener
import com.vjezba.weatherapi.network.ConnectivityMonitor
import com.vjezba.weatherapi.ui.adapters.WeatherAdapter
import com.vjezba.weatherapi.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class WeatherActivity : BaseActivity(R.id.no_internet_layout) {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var weatherAdapter: WeatherAdapter
    val weatherLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    private var isLastPage = false
    private var loading = false
    private var page: Int = 1


    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var permissionRequestGranted = false


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

        checkLocationListenerSettings()

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: kotlin.IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if( grantResults.isNotEmpty() ) {
            permissionRequestGranted =
                requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        getLastLocationListener()
    }

    private fun checkLocationListenerSettings() {
        if (!checkPermissions())
            requestPermission()
        else
            getLastLocationListener()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocationListener() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                addAddressValueToTextView(currentLatLng)
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun addAddressValueToTextView(currentLatLng: LatLng) {
        if( ConnectivityMonitor.isAvailable() ) {
            weatherViewModel.getLastLocationListener(this, currentLatLng)

            weatherViewModel.lastLocation.observe(this, Observer { address ->
                tvCurrentAddress.text = "Current address: " + address.getAddressLine(0)
                if( address.hasLatitude() && address.hasLongitude() )
                    tvLatLongitude.text = "Latitude: " + address.latitude + " Longitude: " + address.longitude
            })
        }
        else {

        }
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