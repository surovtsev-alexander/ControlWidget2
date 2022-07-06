package com.surovtsev.controlwidget2

import android.app.Application
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.widget.helper.ControlWidget2Updater
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import javax.inject.Inject

@HiltAndroidApp
class ControlsWidget2Application: Application() {
    // mandatory components
    @Inject
    lateinit var controlsInfoBroadcastReceiver: ControlsInfoBroadcastReceiver
    @Inject
    lateinit var controlWidget2Updater: ControlWidget2Updater

    override fun onCreate() {
        super.onCreate()

        AndroidLogcatLogger.installOnDebuggableApp(this)
    }
}
