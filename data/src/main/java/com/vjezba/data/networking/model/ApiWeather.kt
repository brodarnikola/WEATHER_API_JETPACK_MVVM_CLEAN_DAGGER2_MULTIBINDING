package com.vjezba.data.networking.model


import com.google.gson.annotations.SerializedName
import com.vjezba.domain.model.MovieResult

data class ApiWeather(

    val code: String = "",

    @SerializedName("list")
    val weatherList: List<ApiWeatherData> = listOf(),
    @SerializedName("city")
    val cityData: ApiCityData = ApiCityData(),
)
