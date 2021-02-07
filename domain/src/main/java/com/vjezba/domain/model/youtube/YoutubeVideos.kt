package com.vjezba.domain.model.youtube



data class YoutubeVideos(

    val id: YoutubeVideoId = YoutubeVideoId(),
    val snippet: YoutubeVideoSnippet = YoutubeVideoSnippet()
)
