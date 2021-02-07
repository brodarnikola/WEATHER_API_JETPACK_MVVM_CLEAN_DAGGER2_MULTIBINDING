package com.vjezba.domain.model



data class Forecast(

    val code: String = "",
    val forecastList: List<ForecastData> = listOf(),
    val cityData: CityData = CityData(),
)
