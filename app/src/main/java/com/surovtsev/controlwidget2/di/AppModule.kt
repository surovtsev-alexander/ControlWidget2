package com.surovtsev.controlwidget2.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.features.controlwidget2.domain.repository.ControlsInformationRepo
import com.surovtsev.controlwidget2.controlwidget2.receiver.helpers.CommandsReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideControlsInfoBroadcastReceiver(
        controlsInformationRepo: ControlsInformationRepo,
        @ApplicationContext context: Context,
    ): ControlsInfoBroadcastReceiver = ControlsInfoBroadcastReceiver(
        controlsInformationRepo
    ).apply {
            registerReceiver(context)
    }

    @Singleton
    @Provides
    fun provideCommandsReceiver(
        wifiManager: WifiManager,
        bluetoothAdapter: BluetoothAdapter,
        @ApplicationContext context: Context,
    ): CommandsReceiver = CommandsReceiver(
        wifiManager,
        bluetoothAdapter
    ).apply {
        registerReceiver(context)
    }
}
