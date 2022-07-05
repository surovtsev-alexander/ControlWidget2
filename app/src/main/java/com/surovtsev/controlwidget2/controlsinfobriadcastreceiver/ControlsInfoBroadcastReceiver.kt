package com.surovtsev.controlwidget2.controlsinfobriadcastreceiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.WifiManager
import logcat.logcat
import javax.inject.Inject

class ControlsInfoBroadcastReceiver @Inject constructor(
//    private val controlsInformationRepo: ControlsInformationRepo,
): BroadcastReceiver() {

    companion object {
        const val wifi_update_action = WifiManager.WIFI_STATE_CHANGED_ACTION

        val actions = listOf(
            WifiManager.WIFI_STATE_CHANGED_ACTION,
            BluetoothAdapter.ACTION_STATE_CHANGED,
            LocationManager.PROVIDERS_CHANGED_ACTION,
        )
    }

    fun registerReceiver(
        context: Context,
    ) {
        val intentFilter = IntentFilter().apply {
            actions.map {
                addAction(it)
            }
        }
        context
            .registerReceiver(this, intentFilter)
        logcat { "registered" }
    }

    override fun onReceive(context: Context, intent: Intent) {
        logcat { "onReceive; intent: $intent" }


        when (intent.action) {
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                logcat { "wifi updated" }
                //controlsInformationRepo.updateWifiState()
            }
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                logcat { "bluetooth updated" }
                //controlsInformationRepo.updateBluetoothState()
            }
            LocationManager.PROVIDERS_CHANGED_ACTION -> {
                logcat { "gps updated" }
                //controlsInformationRepo.updateLocationAdapter()
            }
        }
    }
}