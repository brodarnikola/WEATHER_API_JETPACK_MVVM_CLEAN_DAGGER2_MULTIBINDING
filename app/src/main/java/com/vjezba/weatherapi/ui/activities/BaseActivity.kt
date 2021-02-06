package com.vjezba.weatherapi.ui.activities

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.vjezba.data.networking.ConnectivityUtil
import com.vjezba.weatherapi.App
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.network.ConnectivityChangedEvent
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity(noWifiViewId: Int = 0) : AppCompatActivity() {

    @Inject
    lateinit var connectivityUtil: ConnectivityUtil

    protected var viewLoaded = false

    protected var networkAvailable: Boolean = true
        private set

    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }

    private val noWifiFrame by lazy { if (noWifiViewId != 0) findViewById<FrameLayout>(noWifiViewId) else null }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkStateChangedEvent(connectivityChangedEvent: ConnectivityChangedEvent) {
        networkAvailable = connectivityChangedEvent.networkAvailable
        onNetworkStateUpdated(connectivityChangedEvent.networkAvailable)
    }

    open fun onNetworkStateUpdated(available: Boolean) {}

    override fun onResume() {
        super.onResume()

        App.ref.eventBus.register(this)
        // If needed to check this immediately when application start
        //if( !ConnectivityMonitor.isAvailable() )
        //    App.ref.eventBus.post(ConnectivityChangedEvent(false))
    }

    override fun onPause() {
        super.onPause()
        App.ref.eventBus.unregister(this)
    }

    fun updateConnectivityUi() {
        if (noWifiFrame != null ) {
            noWifiFrame?.visibility = if (networkAvailable) View.GONE else View.VISIBLE
        }
    }
}