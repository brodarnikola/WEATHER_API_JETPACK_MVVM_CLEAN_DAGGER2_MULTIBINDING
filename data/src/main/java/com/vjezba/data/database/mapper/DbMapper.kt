package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBWeather
import com.vjezba.data.networking.model.*
import com.vjezba.domain.model.*


interface DbMapper {

    // weather
    fun mapApiWeatherToDomainWeather(apiWeather: ApiWeather): Weather

    fun mapDomainWeatherToDbWeather(weather: Weather): List<DBWeather>

    fun mapDBWeatherListToWeather(weather: DBWeather): WeatherData


    // movie
    fun mapApiMoviesToDomainMovies(apiNews: ApiMovies): Movies



    fun mapApiMovieDetailsToDomainMovieDetails(apiMovieDetails: ApiMovieDetails): MovieDetails

    fun mapDomainMoviesToDbMovies(newsList: Movies): List<DBWeather>

    fun mapDBMoviesListToMovies(articlesList: DBWeather): MovieResult


    fun mapApiTrailersToDomainTrailers(apiTrailers: ApiTrailers): Trailer

    fun mapApiActorsToDomainActors(apiActors: ApiActors): Actors
}