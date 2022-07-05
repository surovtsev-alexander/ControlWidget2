package com.surovtsev.controlwidget2.features.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelperModule {

    @Singleton
    @Provides
    fun provideWifiManager(
        @ApplicationContext context: Context,
    ): WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Singleton
    @Provides
    fun provideBluetoothAdapter(
        @ApplicationContext context: Context,
    ): BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

//    @Singleton
//    @Provides
//    fun provideLocationManager(
//        @ApplicationContext context: Context,
//    ): LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
}