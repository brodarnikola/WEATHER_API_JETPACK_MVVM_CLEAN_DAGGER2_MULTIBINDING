package com.vjezba.domain.model


data class Weather(

    val weather: List<WeatherData> = listOf(),
    val main: WeatherMain = WeatherMain(),
    val wind: WeatherWind = WeatherWind(),
    val name: String = ""
)
