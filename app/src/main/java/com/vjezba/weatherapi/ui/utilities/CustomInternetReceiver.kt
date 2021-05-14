package com.vjezba.weatherapi.ui.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast


class CustomInternetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("BroadCastReceiverExamp", "" + intent!!.action)

        if (intent.action.equals("com.vjezba.weatherapi.SOME_ACTION"))
            Toast.makeText(
            context,
            "SOME_ACTION is received",
            Toast.LENGTH_LONG
            ).show()
        else if( intent.action.equals("android.intent.action.AIRPLANE_MODE") ) {
            // if getBooleanExtra contains null value,it will directly return back
            val isAirplaneModeEnabled = intent.getBooleanExtra("state", false)
            // checking whether airplane mode is enabled or not
            if (isAirplaneModeEnabled) {
                // showing the toast message if airplane mode is enabled
                Toast.makeText(context, "Airplane Mode Enabled", Toast.LENGTH_LONG).show()
            } else {
                // showing the toast message if airplane mode is disabled
                Toast.makeText(context, "Airplane Mode Disabled", Toast.LENGTH_LONG).show()
            }
        }
        else {
            val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                    || status == BatteryManager.BATTERY_STATUS_FULL

            Toast.makeText(context, "Is mobile phone charging or full: ${isCharging}", Toast.LENGTH_LONG).show()
        }

    }
}
