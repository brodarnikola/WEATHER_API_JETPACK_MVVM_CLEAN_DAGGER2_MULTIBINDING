package com.vjezba.domain.model


import com.google.gson.annotations.SerializedName

data class ForecastData(

    val main: ForecastMain = ForecastMain(),

    val forecast: List<ForecastDescription> = listOf(),

    val wind: ForecastWind = ForecastWind(),

    @SerializedName("dt_txt")
    val dateAndTime: String = String()
)
