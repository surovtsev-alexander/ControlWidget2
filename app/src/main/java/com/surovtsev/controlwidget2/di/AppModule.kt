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


package com.surovtsev.controlwidget2.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.features.controlsinformation.domain.repository.ControlsInformationRepo
import com.surovtsev.controlwidget2.commandsreceiver.CommandsReceiver
import com.surovtsev.controlwidget2.screenbroadcastreceiver.ScreenBroadcastReceiver
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

    @Singleton
    @Provides
    fun provideScreenBroadcastReceiver(
        @ApplicationContext context: Context,
    ): ScreenBroadcastReceiver = ScreenBroadcastReceiver(
    ).apply {
        registerReceiver(context)
    }
}
