/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.data.repository

import android.util.Log
import com.vjezba.data.database.WeatherDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.WeatherRepositoryApi
import com.vjezba.domain.model.*
import com.vjezba.domain.repository.WeatherRepository
import io.reactivex.Flowable
import java.net.URLEncoder

/**
 * RepositoryResponseApi module for handling data operations.
 */

class WeatherRepositoryImpl constructor(
    private val database: WeatherDatabase,
    private val service: WeatherRepositoryApi,
    private val dbMapper: DbMapper?
) : WeatherRepository {

    override fun getWeatherData(latitude: Double, longitude: Double): Flowable<Weather> {

        val appId = "b389e4ccf5ae4bbc8072ccd05c8f85c7"
        val result = service.getWeather(latitude, longitude, appId)

        //Observable.concatArrayEager(newsResult, observableFromDB)

        val correctResult = result.map { dbMapper?.mapApiWeatherToDomainWeather(it)!! }

        return correctResult
    }

    override fun getWeatherDataByCityName(cityName: String): Flowable<Weather> {

        val appId = "b389e4ccf5ae4bbc8072ccd05c8f85c7"
        val cityNameUrlEncoded = URLEncoder.encode(cityName, "utf-8")

        val result = service.getWeatherByCityName(cityNameUrlEncoded, appId)
        val correctResult = result.map { dbMapper?.mapApiWeatherToDomainWeather(it)!! }

        return correctResult
    }

    override fun getForecastData(cityName: String): Flowable<Forecast> {
        val appId = "b389e4ccf5ae4bbc8072ccd05c8f85c7"
        val result = service.getForecast(cityName, appId)

        val correctResult = result.map { dbMapper?.mapApiForecastToDomainForecast(it)!! }

        return correctResult
    }

}
