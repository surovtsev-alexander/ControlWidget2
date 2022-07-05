package com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import com.surovtsev.controlwidget2.ControlInformation

class ControlWidget2UseCaseImp(
    context: Context
): ControlWidget2UseCase {
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override suspend fun getControlInformation(): ControlInformation {
        return ControlInformation(
            wifiManager.isWifiEnabled,
            bluetoothAdapter.isEnabled,
        )
    }
}