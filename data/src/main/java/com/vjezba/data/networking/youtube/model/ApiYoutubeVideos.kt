package com.vjezba.data.networking.youtube.model



data class ApiYoutubeVideos(

    val id: ApiYoutubeVideoId = ApiYoutubeVideoId(),
    val snippet: ApiYoutubeVideoSnippet = ApiYoutubeVideoSnippet()
)
