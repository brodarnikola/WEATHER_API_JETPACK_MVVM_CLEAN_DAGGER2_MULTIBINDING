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
                            it.weather.map {
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
            val descriptionData =  if( it.weather.isNotEmpty() )
                it.weather[0].description
            else
                ""
            DBWeather(
                it.main.temp,
                it.main.feelsLike,
                it.main.tempMax,
                descriptionData,
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
