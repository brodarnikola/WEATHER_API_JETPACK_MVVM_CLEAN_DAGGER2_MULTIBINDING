package com.vjezba.weatherapi.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.AppComponentFactory
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.network.ConnectivityMonitor
import hr.sil.android.zwicktablet.gps.GpsUtils
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashActivity : AppCompatActivity() {


    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var permissionRequestGranted = false

    private val SPLASH_DISPLAY_LENGTH = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch(Dispatchers.IO) {
            delay(SPLASH_DISPLAY_LENGTH)
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                checkLocationSettings()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: kotlin.IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            permissionRequestGranted =
                requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (permissionRequestGranted)
            startApp()
    }

    private fun startApp() {

        if (ConnectivityMonitor.isAvailable() && GpsUtils(this).turnGPSOn()) {
            val intent = Intent(this@SplashActivity, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        } else if (!ConnectivityMonitor.isAvailable()) {
            tvTurnOn.visibility = View.VISIBLE
            tvTurnOn.text = "Please turn on your wifi or mobile data"
            btnRetry.visibility = View.VISIBLE
            btnRetry.setOnClickListener {
                checkIfNetworkAndGpsAreTurnedOn()
            }
        } else if (!GpsUtils(this).turnGPSOn()) {
            tvTurnOn.visibility = View.VISIBLE
            tvTurnOn.text = "Please turn on gps, location service"
            btnRetry.visibility = View.VISIBLE
            btnRetry.setOnClickListener {
                checkIfNetworkAndGpsAreTurnedOn()
            }
        }
    }

    private fun checkIfNetworkAndGpsAreTurnedOn() {
        if (!ConnectivityMonitor.isAvailable()) {
            tvTurnOn.text = "Please turn on your wifi or mobile data"
        } else if (!GpsUtils(this).turnGPSOn()) {
            tvTurnOn.text = "Please turn on gps, location service"
        } else {
            val intent = Intent(this@SplashActivity, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkLocationSettings() {
        if (!checkPermissions())
            requestPermission()
        else
            startApp()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


}