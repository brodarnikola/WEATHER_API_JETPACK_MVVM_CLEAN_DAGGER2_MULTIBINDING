package com.vjezba.domain.model.youtube



data class YoutubeVideosMain(

    val regionCode: String = "",
    val items: List<YoutubeVideos> = listOf()
)
