package com.vjezba.domain.model


import com.google.gson.annotations.SerializedName

data class WeatherMain(

    val temp: String = "",

    @SerializedName("feels_like")
    val feelsLike: String = "",

    @SerializedName("temp_max")
    val tempMax: String = ""
)
