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

package com.vjezba.data.di

import com.vjezba.data.database.WeatherDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.di_dagger2.WeatherNetwork
import com.vjezba.data.di_dagger2.youtube.YoutubeNetwork
import com.vjezba.data.networking.WeatherRepositoryApi
import com.vjezba.data.repository.WeatherRepositoryImpl
import com.vjezba.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class RepositoryModule {

    @Provides
    @WeatherNetwork
    fun provideAllDataFromRestApiNetworkOrFromRoom(weatherDatabase: WeatherDatabase, @WeatherNetwork weatherRepositoryApi: WeatherRepositoryApi, dbMapper : DbMapper) : WeatherRepository {
        return WeatherRepositoryImpl(weatherDatabase, weatherRepositoryApi, dbMapper)
    }
}
