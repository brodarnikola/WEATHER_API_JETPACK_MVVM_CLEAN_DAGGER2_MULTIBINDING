/*
 * Copyright 2020 Google LLC
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

package com.vjezba.domain.repository

import com.vjezba.domain.model.*
import io.reactivex.Flowable


interface WeatherRepository {

    fun getWeatherData(cityName: String) : Flowable<Weather>


    fun getMovies(page: Int) : Flowable<Movies>

    fun getMoviesDetails(movieId: Long) : Flowable<MovieDetails>

    fun getTrailers(movieId: Long) : Flowable<Trailer>

    fun getActors(movieId: Long) : Flowable<Actors>
}
