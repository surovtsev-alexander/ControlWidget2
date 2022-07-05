package com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase

import android.bluetooth.BluetoothAdapter
import android.net.wifi.WifiManager
import com.surovtsev.controlwidget2.features.controlwidget2.domain.model.ControlsInformation
import javax.inject.Inject

class ControlsInformationUseCaseImp @Inject constructor(
    private val wifiManager: WifiManager,
    private val bluetoothAdapter: BluetoothAdapter,
//    private val locationManager: LocationManager,
): ControlsInformationUseCase {

    override suspend fun getControlInformation(): ControlsInformation {
        return ControlsInformation(
            wifiManager.isWifiEnabled,
            bluetoothAdapter.isEnabled,
            false,
//            locationManager.isLocationEnabled,
        )
    }
}