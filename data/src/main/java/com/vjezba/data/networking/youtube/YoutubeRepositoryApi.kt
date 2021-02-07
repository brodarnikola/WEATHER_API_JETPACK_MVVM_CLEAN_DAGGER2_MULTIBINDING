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

package com.vjezba.data.networking.youtube


import com.vjezba.data.networking.youtube.model.ApiYoutubeVideosMain
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface YoutubeRepositoryApi {

    // youtube api key ->   AIzaSyCN7PX5sMGOysm2BzsZ8ml7J-jw6u2jxv0


    @GET("search")
    @Headers("Content-Type: application/json")
    fun getYoutubeVideos( @Query("part") part: String, @Query("maxResults") maxResults: Int,
                          @Query("q") youtubeInsertedKeyword: String,
                          @Query("type") type: String, @Query("key") apiKey: String ): Flowable<ApiYoutubeVideosMain>


}
