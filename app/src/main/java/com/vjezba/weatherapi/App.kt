package com.vjezba.weatherapi

import android.app.Application
import com.vjezba.weatherapi.connectivity.network.ConnectivityChangedEvent
import com.vjezba.weatherapi.connectivity.network.ConnectivityMonitor
import dagger.hilt.android.HiltAndroidApp
import org.greenrobot.eventbus.EventBus

@HiltAndroidApp
class App : Application() {

  init {
    ref = this
  }


  companion object {
    @JvmStatic
    lateinit var ref: App
  }

  //event bus initialization
  val eventBus: EventBus by lazy {
    EventBus.builder()
      .logNoSubscriberMessages(false)
      .sendNoSubscriberEvent(false)
      .build()
  }

  override fun onCreate() {
    super.onCreate()
    //instance = this

    ConnectivityMonitor.initialize(this) { available ->
        eventBus.post(
            ConnectivityChangedEvent(
                available
            )
        )
    }

  }

}

