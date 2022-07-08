package com.surovtsev.controlwidget2.commandsreceiver

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import logcat.logcat

class CommandsReceiver(
    private val wifiManager: WifiManager,
    private val bluetoothAdapter: BluetoothAdapter,
): BroadcastReceiver() {

    fun registerReceiver(
        context: Context
    ) {
        val intentFilter = IntentFilter().apply {
            addAction(COMMAND_FROM_UI)
        }
        context
            .registerReceiver(
                this,
                intentFilter
            )
        logcat { "registered" }
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        logcat { "onReceive; intent: $intent" }

        if (intent.action == COMMAND_FROM_UI) {
            val bundle = intent.extras

            bundle?.keySet()?.map { commandName ->
                val enable = bundle[commandName]?.run { this as Boolean }
                if (enable != null) {
                    when(commandName) {
                        wifiStateKeyDescription.key.name -> {
                            wifiManager.isWifiEnabled = enable
                        }
                        bluetoothStateKeyDescription.key.name -> {
//                            if (ActivityCompat.checkSelfPermission(context,
//                                    Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
//                            ) {
//                                // TODO: Consider calling
//                                //    ActivityCompat#requestPermissions
//                                // here to request the missing permissions, and then overriding
//                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                //                                          int[] grantResults)
//                                // to handle the case where the user grants the permission. See the documentation
//                                // for ActivityCompat#requestPermissions for more details.
//                                return
//                            }
                            if (enable) {
                                bluetoothAdapter.enable()
                            } else {
                                bluetoothAdapter.disable()
                            }
                        }
                        gpsStateKeyDescription.key.name -> {
                            context.startActivity(
                                Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val COMMAND_FROM_UI = "com.surovtsev.controlwidget2.COMMAND_FROM_UI"

        val wifiStateKeyDescription = KeyDescription(
            "WIFI_STATE"
        )
        val bluetoothStateKeyDescription = KeyDescription(
            "BLUETOOTH_STATE"
        )
        val gpsStateKeyDescription = KeyDescription(
            "GPS_STATE"
        )

        data class KeyDescription(
            val key: Preferences.Key<Boolean>,
            val defValue: Boolean = false,
        ) {
            constructor(
                key: String,
                defValue: Boolean = false,
            ): this(
                booleanPreferencesKey(key),
                defValue,
            )

            fun getValueOrDefault(
                pref: Preferences,
            ): Boolean {
                return pref[key] ?: defValue
            }
        }
    }
}