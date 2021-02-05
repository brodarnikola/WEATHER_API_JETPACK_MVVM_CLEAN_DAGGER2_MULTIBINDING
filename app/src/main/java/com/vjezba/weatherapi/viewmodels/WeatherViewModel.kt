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

import android.content.ContentValues
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.vjezba.data.database.WeatherDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.ConnectivityUtil
import com.vjezba.domain.model.CityData
import com.vjezba.domain.model.MovieResult
import com.vjezba.domain.model.Weather
import com.vjezba.domain.model.WeatherData
import com.vjezba.domain.repository.WeatherRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class WeatherViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository,
    private val dbWeather: WeatherDatabase,
    private val dbMapper: DbMapper?,
    private val connectivityUtil: ConnectivityUtil
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _weatherMutableLiveData = MutableLiveData<Weather>().apply {
        value = Weather( "", listOf(), CityData("", 0L) )
    }

    val weatherList: LiveData<Weather> = _weatherMutableLiveData

    fun getWeatherForeastDataFromRestApi(cityName: String) {
        if (connectivityUtil.isConnectedToInternet())
            getWeatherFromNetwork(cityName)
        else
            getWeatherFromLocalStorage()
    }

    private fun getWeatherFromNetwork(cityName: String) {
        weatherRepository.getWeatherData(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<Weather> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(response: Weather) {

                    insertWeatherIntoDatabase(response)

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

    private fun getWeatherFromLocalStorage() {
        Observable.fromCallable {
            val listDBMovies = getMoviesFromDb()
            Weather("", listDBMovies, CityData())
        }
            .subscribeOn(Schedulers.io())
            //.flatMap { source: List<Articles> -> Observable.fromIterable(source) } // this flatMap is good if you want to iterate, go through list of objects.
            //.flatMap { source: News? -> Observable.fromArray(source) or  } // .. iterate through each item
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { data: Weather? ->

                Log.i("Size of database", "")
                _weatherMutableLiveData.value?.let { _ ->
                    _weatherMutableLiveData.value = data
                }
            }
            .subscribe()
    }

    private fun getMoviesFromDb(): List<WeatherData> {
        return dbWeather.weatherDAO().getWeather().map {
            dbMapper?.mapDBWeatherListToWeather(it) ?: WeatherData()
        }
    }

    private fun insertWeatherIntoDatabase(weather: Weather) {

        Observable.fromCallable {

            val weather = dbMapper?.mapDomainWeatherToDbWeather(weather) ?: listOf()
            dbWeather.weatherDAO().updateWeather(
                weather
            )
            Log.d(
                "da li ce uci unutra * ",
                "da li ce uci unutra, spremiti podatke u bazu podataka: " + toString()
            )
        }
            .doOnError { Log.e("Error in observables", "Error is: ${it.message}, ${throw it}") }
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d(
                    "Hoce spremiti vijesti",
                    "Inserted ${weather.weatherList.size} movies from API in DB..."
                )
            }
    }

    private val _lastLocationMutableLiveData = MutableLiveData<Address>().apply {
        value = Address( Locale("EN") )
    }

    val lastLocation: LiveData<Address> = _lastLocationMutableLiveData

    fun getLastLocationListener( context: Context, currentLatLng: LatLng) {

        //weatherRepository.getLastLocationListener(context, currentLatLng)

        viewModelScope.launch(Dispatchers.IO) {
            val locale = Locale( getSystemLanguage(context) )
            val gcd = Geocoder(context, locale)

            var addresses: MutableList<Address> = mutableListOf()
            try {
                addresses =
                    gcd.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1)
            } catch (e: Exception) {
                Log.i( "ErrorTag","Exception is: ${e}")
            }
            withContext(Dispatchers.Main) {
                if (addresses != null && addresses.size > 0 && addresses[0] != null
                    && addresses[0].getAddressLine(0) != null )
                    _lastLocationMutableLiveData.value = addresses[0]
            }
        }
    }

    private fun getSystemLanguage(context: Context) : String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            context.resources.configuration.locales.get(0).language.toString()
        } else{
            context.resources.configuration.locale.language.toString()
        }
    }


    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

}

