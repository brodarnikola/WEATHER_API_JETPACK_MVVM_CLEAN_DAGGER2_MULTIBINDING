package com.vjezba.data.networking.model


import com.google.gson.annotations.SerializedName
import com.vjezba.domain.model.MovieResult
import java.util.*

data class ApiWeatherData(

    val main: ApiWeatherMain = ApiWeatherMain(),

    val weather: List<ApiWeatherDescription> = listOf(),

    val wind: ApiWeatherWind = ApiWeatherWind(),

    @SerializedName("dt_txt")
    val dateAndTime: String = String()
)
