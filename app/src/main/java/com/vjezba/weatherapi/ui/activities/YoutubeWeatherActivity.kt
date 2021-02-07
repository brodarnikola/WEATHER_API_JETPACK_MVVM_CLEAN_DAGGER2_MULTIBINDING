package com.vjezba.weatherapi.ui.activities

import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.vjezba.weatherapi.R
import kotlinx.android.synthetic.main.activity_youtube_weather.*


class YoutubeWeatherActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener   {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_weather)
        youtube_player_view.initialize(resources.getString(R.string.youtube_api_key), this)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        if (!wasRestored) {
            //player?.cueVideo("wKJ9KzGQq0w")
            player?.loadVideo("wKJ9KzGQq0w")
            player?.play()
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        //showShortToast("Youtube Api Initialization Failure")
    }

}