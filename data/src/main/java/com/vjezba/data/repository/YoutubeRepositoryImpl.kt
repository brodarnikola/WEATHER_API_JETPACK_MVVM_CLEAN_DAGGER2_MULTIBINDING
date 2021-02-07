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

import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.youtube.YoutubeRepositoryApi
import com.vjezba.domain.model.youtube.YoutubeVideosMain
import com.vjezba.domain.repository.YoutubeRepository
import io.reactivex.Flowable

/**
 * RepositoryResponseApi module for handling data operations.
 */

class YoutubeRepositoryImpl constructor(
    private val service: YoutubeRepositoryApi,
    private val dbMapper: DbMapper?
) : YoutubeRepository {

    override fun getYoutubeVideosFromKeyWord(part: String, maxResults: Int, youtubeKeyWord: String, type: String): Flowable<YoutubeVideosMain> {

        val result = service.getYoutubeVideos( part, maxResults, youtubeKeyWord, type, "AIzaSyCN7PX5sMGOysm2BzsZ8ml7J-jw6u2jxv0")

        val correctResults = result.map { dbMapper?.mapApiYoutubeVideosToDomainYoutube(it)!! }
        return correctResults
        TODO("Not yet implemented")
    }

//    @Query("part") part: String, @Query("maxResults") maxResults: Int,
//    @Query("q") youtubeInsertedKeyword: String,
//    @Query("type") type: String, @Query("key") apiKey: String ): Flowable<ApiWeather>

//    override fun getWeatherData(latitude: Double, longitude: Double): Flowable<Weather> {
//
//        //val appId = "b389e4ccf5ae4bbc8072ccd05c8f85c7"
//
//        val result = service.getWeather(latitude, longitude, API_KEY_FOR_OPEN_WEATHER)
//
//        //Observable.concatArrayEager(newsResult, observableFromDB)
//
//        val correctResult = result.map { dbMapper?.mapApiWeatherToDomainWeather(it)!! }
//
//        return correctResult
//    }
//
//    override fun getWeatherDataByCityName(cityName: String): Flowable<Weather> {
//
//        //val appId = "b389e4ccf5ae4bbc8072ccd05c8f85c7"
//        val cityNameUrlEncoded = URLEncoder.encode(cityName, "utf-8")
//
//        val result = service.getWeatherByCityName(cityNameUrlEncoded, API_KEY_FOR_OPEN_WEATHER)
//        val correctResult = result.map { dbMapper?.mapApiWeatherToDomainWeather(it)!! }
//
//        return correctResult
//    }
//
//    override fun getForecastData(cityName: String): Flowable<Forecast> {
//        val result = service.getForecast(cityName, API_KEY_FOR_OPEN_WEATHER)
//
//        val correctResult = result.map { dbMapper?.mapApiForecastToDomainForecast(it)!! }
//
//        return correctResult
//    }

}
