package com.vjezba.weatherapi.ui.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.vjezba.domain.model.Weather
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.databinding.FragmentWeatherBinding
import com.vjezba.weatherapi.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WeatherFragment : Fragment() {

    val weatherViewModel: WeatherViewModel by viewModels()

    var cityName = ""
    var searchNewCityData = false

    private lateinit var locationCallback: LocationCallback
    private var fusedLocationClient: FusedLocationProviderClient? = null

    lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.tvToolbarTitle?.text = "WEATHER"

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        createLocationRequest()

        lifecycleScope.launch(Dispatchers.Main) {
            delay(4000)
            setupLiveDataAndObservePattern()
        }
        //weatherViewModel.getLastLocationListener(requireContext(), fusedLocationClient)

        setupClickListener()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun createLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    // ...
                    if(location != null) {
                        Log.i("Tag", "New location received: ${location}")
                        weatherViewModel.getLastLocationListener(requireContext(), fusedLocationClient)
                        fusedLocationClient?.removeLocationUpdates(this)
                    }
                }
            }
        }

        fusedLocationClient?.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private fun setupClickListener() {
        binding.btnStartForecast.setOnClickListener {
            val direction =
                WeatherFragmentDirections.weatherFragmentToForecastFragment(
                    cityName
                )
            findNavController().navigate(direction)
        }

        binding.btnSearchWeatherByCityName.setOnClickListener {
            it.isEnabled = false
            it.alpha = 0.4f
            searchNewCityData = true
            weatherViewModel.getWeatherDataByCityName(etCityName.text.toString())
        }
    }

    private fun setupLiveDataAndObservePattern() {

        weatherViewModel.lastLocation.observe(viewLifecycleOwner, Observer { address ->

            clDisplayDataByCurrentLocation.visibility = View.GONE
            rlSearchDataByCurrentLocation.visibility = View.VISIBLE

            tvCurrentAddress.text = "Current address: " + address.getAddressLine(0)
            if (address.hasLatitude() && address.hasLongitude())
                tvLatLongitude.text =
                    "Latitude: " + address.latitude + " Longitude: " + address.longitude
        })

        weatherViewModel.weatherList.observe(viewLifecycleOwner, Observer { item ->

            Log.d(ContentValues.TAG, "Weather data: ${item.weather.joinToString { "-" }}")
            rlSearchDataByCurrentLocation.visibility = View.GONE
            clDisplayDataByCurrentLocation.visibility = View.VISIBLE

            if( searchNewCityData ) {
                binding.btnSearchWeatherByCityName.isEnabled = true
                binding.btnSearchWeatherByCityName.alpha = 1.0f
            }

            if( item.main.temp == "" && item.name == "" ) {
                notFoundAnyCityWithInsertedText()
            }
            else {
                setCurrentCityLocation(item)
            }
        })
    }

    private fun setCurrentCityLocation(item: Weather) {
        tvCurrentAddress.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.sunflower_black
            )
        )
        if( searchNewCityData ) {
            tvCurrentAddress.text = "Searched city name is: " + etCityName.text.toString()
        }

        if (item.weather.isNotEmpty()) {
            tvDescription.visibility = View.VISIBLE
            tvDescription.text = "Description: " + item.weather[0].description
        } else
            tvDescription.visibility = View.GONE

        tvTemp.text = "Temp: " + item.main.temp
        tvMax.text = "Temp max: " + item.main.tempMax
        tvFeelsLike.text = "Feels like: " + item.main.feelsLike
        tvWind.text = "Wind Speed: " + item.wind.speed
        cityName = item.name
    }

    private fun notFoundAnyCityWithInsertedText() {
        tvCurrentAddress.setTextColor(ContextCompat.getColor( requireContext(), android.R.color.holo_red_dark ))
        tvCurrentAddress.text = "No match, no city found with this inserted text"

        tvDescription.text = "Description: -"
        tvTemp.text = "Temp: -"
        tvMax.text = "Temp max: -"
        tvFeelsLike.text = "Feels like: -"
        tvWind.text = "Wind Speed: -"
    }


}