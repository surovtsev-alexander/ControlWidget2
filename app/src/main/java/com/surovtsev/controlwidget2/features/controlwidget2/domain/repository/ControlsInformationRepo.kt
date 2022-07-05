package com.surovtsev.controlwidget2.features.controlwidget2.domain.repository

import android.bluetooth.BluetoothAdapter
import android.location.LocationManager
import android.net.wifi.WifiManager
import com.surovtsev.controlwidget2.features.controlwidget2.domain.model.ControlsInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import logcat.logcat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ControlsInformationRepo @Inject constructor(
    private val wifiManager: WifiManager,
    private val bluetoothAdapter: BluetoothAdapter,
    private val locationAdapter: LocationManager,
) {
    private val _controlsInfoStateFlow = MutableStateFlow(
        ControlsInformation(
            false,
            false,
            false,
        )
    )
    val controlsInfoStateFlow = _controlsInfoStateFlow.asStateFlow()

    fun updateWifiState() {
        updateControlsInfoStateFlow {
            it.copy(wifiEnabled = wifiManager.isWifiEnabled)
        }
    }

    fun updateBluetoothState() {
        updateControlsInfoStateFlow {
            it.copy(bluetoothEnabled = bluetoothAdapter.isEnabled)
        }
    }

    fun updateLocationAdapter() {
        updateControlsInfoStateFlow {
            it.copy(gpsEnabled = locationAdapter.isLocationEnabled)
        }
    }

    private fun updateControlsInfoStateFlow(
        action: (curr: ControlsInformation) -> ControlsInformation
    ) {
        _controlsInfoStateFlow.update {
            action(it)
        }
        logcat { controlsInfoStateFlow.value.toString() }
    }
}
