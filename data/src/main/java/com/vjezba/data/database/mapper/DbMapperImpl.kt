package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBWeather
import com.vjezba.data.networking.model.*
import com.vjezba.data.networking.youtube.model.ApiYoutubeVideosMain
import com.vjezba.domain.DataState
import com.vjezba.domain.model.*
import com.vjezba.domain.model.youtube.*

class DbMapperImpl : DbMapper {

    override fun mapApiWeatherToDomainWeather(apiWeather: ApiWeather): DataState<Weather> {
        return with(apiWeather) {
            DataState.Success(
                Weather(
                    weather.map {
                        WeatherData( it.description )
                    },
                    WeatherMain( main.temp, main.feelsLike, main.tempMax, main.tempMin, main.humidity ),
                    WeatherWind( wind.speed ),
                    name
                )
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

    override fun mapApiYoutubeVideosToDomainYoutube(youtubeVideosMain: ApiYoutubeVideosMain): YoutubeVideosMain {

        return with(youtubeVideosMain) {
            YoutubeVideosMain(
                nextPageToken,
                regionCode,
//                items.map {
//                    it.id.kind
//                }
                items.map {

                        YoutubeVideos(
                            YoutubeVideoId(it.id.kind, it.id.videoId),
                            YoutubeVideoSnippet(
                                it.snippet.title, it.snippet.description,
                                YoutubeVideoThumbnails(
                                    YoutubeVideoThumbnailsMedium(
                                        it.snippet.thumbnails.medium.url,
                                        it.snippet.thumbnails.medium.width,
                                        it.snippet.thumbnails.medium.height
                                    )
                                )
                        ))

                }
            )
        }

    }


}
