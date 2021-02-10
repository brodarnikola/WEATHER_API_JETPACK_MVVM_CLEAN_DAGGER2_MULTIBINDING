package com.vjezba.weatherapi.ui.activities

import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.vjezba.data.networking.ConnectivityUtil
import com.vjezba.weatherapi.App
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.connectivity.network.ConnectivityChangedEvent
import dagger.hilt.android.AndroidEntryPoint
import hr.sil.android.zwicktablet.gps.LocationGpsMonitor
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity(noWifiViewId: Int = 0, noLocationGpsViewId: Int = 0) : AppCompatActivity() {

    @Inject
    lateinit var connectivityUtil: ConnectivityUtil

    protected var viewLoaded = false

    protected var networkAvailable: Boolean = true
        private set

    protected var locationGPSAvailable: Boolean = true

    private var locationGPSListenerKey: String? = null

    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }

    private val noWifiFrame by lazy { if (noWifiViewId != 0) findViewById<FrameLayout>(noWifiViewId) else null }
    private val noGpsLocationFrame by lazy { if (noLocationGpsViewId != 0) findViewById<FrameLayout>(noLocationGpsViewId) else null }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkStateChangedEvent(connectivityChangedEvent: ConnectivityChangedEvent) {
        networkAvailable = connectivityChangedEvent.networkAvailable
        onNetworkStateUpdated(connectivityChangedEvent.networkAvailable)
    }

    open fun onNetworkStateUpdated(available: Boolean) {}
    open fun onGpsLocationServiceStateUpdated(available: Boolean) {}

    override fun onResume() {
        super.onResume()

        if (locationGPSListenerKey == null) {
            locationGPSListenerKey = LocationGpsMonitor(this).addListener { available ->
                uiHandler.post { onGpsLocationServiceStateUpdated(available) }
            }
        }
        App.ref.eventBus.register(this)
        // If needed to check this immediately when application start
        //if( !ConnectivityMonitor.isAvailable() )
        //    App.ref.eventBus.post(ConnectivityChangedEvent(false))
    }

    override fun onPause() {
        super.onPause()
        App.ref.eventBus.unregister(this)
        locationGPSListenerKey?.let { LocationGpsMonitor(this).removeListener(it) }
        locationGPSListenerKey = null
    }

    fun updateConnectivityUi() {


        if ( noWifiFrame != null && noGpsLocationFrame != null) {
            noWifiFrame?.visibility = if (networkAvailable) View.GONE else View.VISIBLE
            noGpsLocationFrame?.visibility = if (locationGPSAvailable) View.GONE else {
                if (!networkAvailable) View.GONE else View.VISIBLE
            }
        }


        if (noWifiFrame != null ) {
            noWifiFrame?.visibility = if (networkAvailable) View.GONE else View.VISIBLE
        }
    }

    fun showSnackbarSync(message: String, isLong: Boolean, mainContentRoot: View) {
        val snackbar = Snackbar.make(
            mainContentRoot,
            message,
            if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
        )

        val textColor = ContextCompat.getColor(this, R.color.sunflower_yellow_500)

        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.sunflower_green_300) )

        val snackbarText = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarText.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        snackbarText.setTextColor(textColor)
        snackbarText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        snackbarText.textSize = 19f
        snackbar.show()
    }

}