package com.vjezba.data.networking.youtube.model



data class ApiYoutubeVideosMain(

    val regionCode: String = "",
    val items: List<ApiYoutubeVideos> = listOf()
)
