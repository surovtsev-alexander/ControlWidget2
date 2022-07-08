/*
MIT License

Copyright (c) [2022] [Alexander Surovtsev]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */


package com.surovtsev.controlwidget2.controlsinfobriadcastreceiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.WifiManager
import com.surovtsev.controlwidget2.features.controlsinformation.domain.repository.ControlsInformationRepo
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