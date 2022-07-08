package com.surovtsev.controlwidget2.controlsinfobriadcastreceiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.WifiManager
import com.surovtsev.controlwidget2.features.controlwidget2.domain.repository.ControlsInformationRepo
import logcat.logcat


class ControlsInfoBroadcastReceiver(
    private val controlsInformationRepo: ControlsInformationRepo,
): BroadcastReceiver() {

    companion object {
        const val INIT_ACTION = "com.surovtsev.controlwidget2.init_state"

        val actions = listOf(
            INIT_ACTION,
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
            INIT_ACTION -> {
                logcat { "init action" }
                controlsInformationRepo.init()
            }
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                logcat { "wifi updated" }
                controlsInformationRepo.updateWifiState()
            }
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                logcat { "bluetooth updated" }
                controlsInformationRepo.updateBluetoothState()
            }
            LocationManager.PROVIDERS_CHANGED_ACTION -> {
                logcat { "gps updated" }
                controlsInformationRepo.updateLocationManager()
            }
        }
    }
}