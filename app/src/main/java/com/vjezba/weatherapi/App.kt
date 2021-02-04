package com.vjezba.weatherapi

import android.app.Activity
import android.app.Application
import com.vjezba.weatherapi.network.ConnectivityChangedEvent
import com.vjezba.weatherapi.network.ConnectivityMonitor
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.hilt.android.HiltAndroidApp
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

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
      eventBus.post(ConnectivityChangedEvent(available))
    }

  }

}

