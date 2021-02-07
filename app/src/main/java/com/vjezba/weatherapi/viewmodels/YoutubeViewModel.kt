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
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vjezba.data.database.WeatherDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.domain.model.CityData
import com.vjezba.domain.model.Forecast
import com.vjezba.domain.model.ForecastData
import com.vjezba.domain.repository.WeatherRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class YoutubeViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository,
    private val dbWeather: WeatherDatabase,
    private val dbMapper: DbMapper?
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _forecastMutableLiveData = MutableLiveData<Forecast>().apply {
        value = Forecast("", listOf(), CityData("", 0L))
    }

    val forecastList: LiveData<Forecast> = _forecastMutableLiveData

    fun getForecastFromNetwork(cityName: String) {
        weatherRepository.getForecastData(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<Forecast> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(response: Forecast) {

                    insertWeatherIntoDatabase(response)

                    _forecastMutableLiveData.value?.let {
                        _forecastMutableLiveData.value = response
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

    private fun insertWeatherIntoDatabase(forecast: Forecast) {

        Observable.fromCallable {

            val weather = dbMapper?.mapDomainWeatherToDbWeather(forecast) ?: listOf()
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
                    "Inserted ${forecast.forecastList.size} forecast data from API into DB..."
                )
            }
    }

    fun getWeatherFromLocalStorage() {
        Observable.fromCallable {
            val listForecast = getForecastFromDB()
            Forecast("", listForecast, CityData())
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { data: Forecast? ->

                Log.i("Size of database", "")
                _forecastMutableLiveData.value?.let { _ ->
                    _forecastMutableLiveData.value = data
                }
            }
            .subscribe()
    }

    private fun getForecastFromDB(): List<ForecastData> {
        return dbWeather.weatherDAO().getWeather().map {
            dbMapper?.mapDBWeatherListToWeather(it) ?: ForecastData()
        }
    }


    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

}

