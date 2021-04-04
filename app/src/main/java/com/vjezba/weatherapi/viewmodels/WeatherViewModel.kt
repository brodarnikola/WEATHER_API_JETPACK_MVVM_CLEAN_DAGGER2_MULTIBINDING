/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.weatherapi.viewmodels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.vjezba.data.di_dagger2.WeatherNetwork
import com.vjezba.domain.ResultState
import com.vjezba.domain.repository.WeatherRepository
import com.vjezba.weatherapi.App
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class WeatherViewModel @ViewModelInject constructor(
    @WeatherNetwork private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var locationCallback: LocationCallback
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val _weatherMutableLiveData: MutableLiveData<ResultState<*>> = MutableLiveData()

    val weatherList: LiveData<ResultState<*>> = _weatherMutableLiveData

    private fun getWeatherFromNetwork(latitude: Double, longitude: Double) {
        weatherRepository.getWeatherData(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<ResultState<*>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(newWeatherData: ResultState<*>) {
                    _weatherMutableLiveData.value = newWeatherData
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        ContentValues.TAG,
                        "onError received: " + e.message
                    )
                }

                override fun onComplete() {

                }
            })
    }

    fun getWeatherDataByCityName(cityName: String) {
        weatherRepository.getWeatherDataByCityName(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .onErrorResumeNext { throwable: Throwable ->
                return@onErrorResumeNext ObservableSource {

                    _weatherMutableLiveData.value?.let {
                        _weatherMutableLiveData.value = ResultState.Error(throwable as java.lang.Exception)
                    }
                    Log.d("onErrorResumeNext", "on exception resume next, update ui" + throwable.localizedMessage)
                }
            }
//            .onExceptionResumeNext {
//                _weatherMutableLiveData.value?.let {
//                    _weatherMutableLiveData.value = it
//                }
//                Log.d("onExceptionResumeNext","on exception resume next, update ui")
//            }
            .subscribe(object : Observer<ResultState<*>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(response: ResultState<*>) {

                    _weatherMutableLiveData.value?.let {
                        _weatherMutableLiveData.value = response
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        ContentValues.TAG,
                        "onError received: " + e.message
                    )
                }

                override fun onComplete() {

                }
            })
    }

    private val _lastLocationMutableLiveData = MutableLiveData<Address>().apply {
        value = Address(Locale("EN"))
    }

    val lastLocation: LiveData<Address> = _lastLocationMutableLiveData

    @SuppressLint("MissingPermission")
    fun getLastLocationListener(
        context: Context,
        fusedLocationClient: FusedLocationProviderClient?
    ) {

        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                addAddressValueToTextView( context, currentLatLng)
            }
        }
    }

    private fun addAddressValueToTextView(context: Context, currentLatLng: LatLng) {
        val locale = Locale(getSystemLanguage(context))
        val gcd = Geocoder(context, locale)

        var address: MutableList<Address> = mutableListOf()
        try {
            address =
                gcd.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1)
        } catch (e: Exception) {
            Log.i("ErrorTag", "Exception is: ${e}")
        }
        if (address != null && address.size > 0 && address[0] != null
            && address[0].getAddressLine(0) != null
        ) {
            _lastLocationMutableLiveData.value = address[0]
            getWeatherFromNetwork(address[0].latitude, address[0].longitude)
        }
    }

    private fun getSystemLanguage(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0).language.toString()
        } else {
            context.resources.configuration.locale.language.toString()
        }
    }

    fun checkIfCurrentLocationAlreadyFetched(): Boolean {
        return if (!App.ref.getCurrentLocationOnlyOnce) {
            App.ref.getCurrentLocationOnlyOnce = true
            return false
        } else {
            return true
        }
    }

    @SuppressLint("MissingPermission")
    fun setupLocationRequest( context: Context, activity: FragmentActivity ) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
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
                        getLastLocationListener(context, fusedLocationClient)
                        fusedLocationClient?.removeLocationUpdates(this)
                    }
                }
            }
        }

        fusedLocationClient?.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    fun stopLocationRequest() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

}

