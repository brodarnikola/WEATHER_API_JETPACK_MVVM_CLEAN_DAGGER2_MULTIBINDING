package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBWeather
import com.vjezba.data.networking.model.*
import com.vjezba.data.networking.youtube.model.ApiYoutubeVideosMain
import com.vjezba.domain.ResultState
import com.vjezba.domain.model.*
import com.vjezba.domain.model.youtube.YoutubeVideosMain


interface DbMapper {

    // forecast
    fun mapApiWeatherToDomainWeather(apiForecast: ApiWeather): ResultState<Weather>

    fun mapApiForecastToDomainForecast(apiForecast: ApiForecast): ResultState<Forecast>

    fun mapDomainWeatherToDbWeather(forecast: Forecast): List<DBWeather>

    fun mapDBWeatherListToWeather(weather: DBWeather): ForecastData


    fun mapApiYoutubeVideosToDomainYoutube( youtubeVideosMain: ApiYoutubeVideosMain): YoutubeVideosMain
}