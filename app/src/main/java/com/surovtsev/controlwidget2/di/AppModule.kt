package com.surovtsev.controlwidget2.di

import android.content.Context
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.features.controlwidget2.domain.repository.ControlsInformationRepo
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
        @ApplicationContext context: Context,
        controlsInformationRepo: ControlsInformationRepo,
    ): ControlsInfoBroadcastReceiver = ControlsInfoBroadcastReceiver(
        controlsInformationRepo
    ).apply {
            registerReceiver(context)
    }
}
