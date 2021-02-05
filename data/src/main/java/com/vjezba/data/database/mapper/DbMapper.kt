package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBWeather
import com.vjezba.data.networking.model.*
import com.vjezba.domain.model.*


interface DbMapper {

    // forecast
    fun mapApiWeatherToDomainWeather(apiForecast: ApiWeather): Weather

    fun mapApiForecastToDomainForecast(apiForecast: ApiForecast): Forecast

    fun mapDomainWeatherToDbWeather(forecast: Forecast): List<DBWeather>

    fun mapDBWeatherListToWeather(weather: DBWeather): ForecastData


    // movie
    fun mapApiMoviesToDomainMovies(apiNews: ApiMovies): Movies



    fun mapApiMovieDetailsToDomainMovieDetails(apiMovieDetails: ApiMovieDetails): MovieDetails

    fun mapDomainMoviesToDbMovies(newsList: Movies): List<DBWeather>

    fun mapDBMoviesListToMovies(articlesList: DBWeather): MovieResult


    fun mapApiTrailersToDomainTrailers(apiTrailers: ApiTrailers): Trailer

    fun mapApiActorsToDomainActors(apiActors: ApiActors): Actors
}