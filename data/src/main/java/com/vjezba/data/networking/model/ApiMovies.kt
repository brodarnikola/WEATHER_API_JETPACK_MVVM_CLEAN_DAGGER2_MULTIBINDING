package com.vjezba.data.networking.model


import com.google.gson.annotations.SerializedName
import com.vjezba.domain.model.MovieResult

data class ApiMovies(

    @SerializedName("page")
    val page: Int = 0,
    val results: List<MovieResult> = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("total_results")
    val totalResults: Long = 0L
)
