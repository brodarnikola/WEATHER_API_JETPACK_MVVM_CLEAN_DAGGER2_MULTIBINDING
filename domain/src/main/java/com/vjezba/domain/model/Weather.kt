package com.vjezba.domain.model



data class Weather(

    val code: String = "",
    val weatherList: List<WeatherData> = listOf(),
    val cityData: CityData = CityData(),
)
