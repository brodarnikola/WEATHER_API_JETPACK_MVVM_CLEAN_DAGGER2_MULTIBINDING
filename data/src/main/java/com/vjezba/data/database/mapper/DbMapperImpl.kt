/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBWeather
import com.vjezba.data.networking.model.*
import com.vjezba.domain.model.*

class DbMapperImpl : DbMapper {



    override fun mapApiWeatherToDomainWeather(apiWeather: ApiWeather): Weather {
        return with(apiWeather) {
            Weather(
                weather.map {
                    WeatherData( it.description )
                },
                WeatherMain( main.temp, main.feelsLike, main.tempMax, main.tempMin, main.humidity ),
                WeatherWind( wind.speed ),
                name
            )
        }
    }

    override fun mapApiForecastToDomainForecast(apiForecast: ApiForecast): Forecast {
        return with(apiForecast) {
            Forecast(
                code,
                forecastList.map {
                    with(it) {
                        ForecastData(
                            ForecastMain(it.main.temp, it.main.feelsLike, it.main.tempMax),
                            it.forecast.map {
                                ForecastDescription(it.description)
                            },
                            ForecastWind(it.wind.speed),
                            dateAndTime
                        )
                    }
                },
                CityData(cityData.country, cityData.population)
            )
        }
    }

    override fun mapDomainWeatherToDbWeather(forecast: Forecast): List<DBWeather> {
        return forecast.forecastList.map {
            DBWeather(
                it.main.temp,
                it.main.feelsLike,
                it.main.tempMax,
                it.forecast[0].description,
                it.wind.speed,
                it.dateAndTime
            )
        }
    }

    override fun mapDBWeatherListToWeather(weather: DBWeather): ForecastData {
        return with(weather) {
            ForecastData(
                ForecastMain( weather.temp, weather.feelsLike, weather.tempMax ),
                listOf(ForecastDescription( weather.description )),
                ForecastWind(weather.speed),
                dateAndTime
            )
        }
    }


    override fun mapApiMoviesToDomainMovies(apiMovies: ApiMovies): Movies {
        return with(apiMovies) {
            Movies(
                page,
                results,
                totalPages,
                totalResults
            )
        }
    }

    override fun mapApiMovieDetailsToDomainMovieDetails(apiMovieDetails: ApiMovieDetails): MovieDetails {
        return with(apiMovieDetails) {
            MovieDetails(
                id,
                adult,
                backdropPath,
                budget,
                homepage,
                originalLanguage,
                originalTitle,
                overview,
                popularity,
                releaseDate
            )
        }
    }

    override fun mapDomainMoviesToDbMovies(moviesList: Movies): List<DBWeather> {
//        return moviesList.result.map {
//            with(it) {
//                DBWeather(
//                    id ?: 0,
//                    idOfMovie = it.id ?: 0L,
//                    backdropPath = it.backdropPath,
//                    originalLanguage = it.originalLanguage,
//                    originalTitle = it.originalTitle,
//                    overview = it.overview,
//                    popularity = it.popularity
//                )
//            }
//        }
        return listOf()
    }

    override fun mapDBMoviesListToMovies(weatherList: DBWeather): MovieResult {
//        return with(forecastList) {
//            MovieResult(
//                id = id,
//                backdropPath = backdropPath,
//                originalLanguage = originalLanguage,
//                originalTitle = originalTitle,
//                overview = overview,
//                popularity = popularity
//            )
//        }
        return MovieResult()
    }

    override fun mapApiTrailersToDomainTrailers(apiTrailers: ApiTrailers): Trailer {
        return with(apiTrailers) {
            Trailer(
                id,
                results
            )
        }
    }

    override fun mapApiActorsToDomainActors(apiActors: ApiActors): Actors {
        return with(apiActors) {
            Actors(
                id,
                cast
            )
        }
    }

}
