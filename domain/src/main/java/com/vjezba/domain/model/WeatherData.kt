package com.vjezba.domain.model


import com.google.gson.annotations.SerializedName
import com.vjezba.domain.model.MovieResult
import java.util.*

data class WeatherData(

    val main: WeatherMain = WeatherMain(),

    val weather: List<WeatherDescription> = listOf(),

    val wind: WeatherWind = WeatherWind(),

    @SerializedName("dt_txt")
    val dateAndTime: String = String()
)
