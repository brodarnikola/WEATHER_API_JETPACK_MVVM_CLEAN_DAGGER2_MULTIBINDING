

package com.vjezba.data.repository

import com.vjezba.data.API_KEY_FOR_OPEN_WEATHER
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

        val result = service.getWeather(latitude, longitude, API_KEY_FOR_OPEN_WEATHER)

        //Observable.concatArrayEager(newsResult, observableFromDB)

        val correctResult = result.map { dbMapper?.mapApiWeatherToDomainWeather(it)!! }

        return correctResult
    }

    override fun getWeatherDataByCityName(cityName: String): Flowable<Weather> {

        val cityNameUrlEncoded = URLEncoder.encode(cityName, "utf-8")

        val result = service.getWeatherByCityName(cityNameUrlEncoded, API_KEY_FOR_OPEN_WEATHER)
        val correctResult = result.map { dbMapper?.mapApiWeatherToDomainWeather(it)!! }

        return correctResult
    }

    override fun getForecastData(cityName: String): Flowable<Forecast> {
        val result = service.getForecast(cityName, API_KEY_FOR_OPEN_WEATHER)

        val correctResult = result.map { dbMapper?.mapApiForecastToDomainForecast(it)!! }

        return correctResult
    }

}
