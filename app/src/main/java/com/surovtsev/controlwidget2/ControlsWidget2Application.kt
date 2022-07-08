package com.surovtsev.controlwidget2

import android.app.Application
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.controlwidget2.helper.ControlWidget2Updater
import com.surovtsev.controlwidget2.controlwidget2.receiver.helpers.CommandsReceiver
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.logcat
import javax.inject.Inject

@HiltAndroidApp
class ControlsWidget2Application: Application() {
    // mandatory components
    @Inject
    lateinit var controlsInfoBroadcastReceiver: ControlsInfoBroadcastReceiver
    @Inject
    lateinit var controlWidget2Updater: ControlWidget2Updater
    @Inject
    lateinit var commandsReceiver: CommandsReceiver

    override fun onCreate() {
        super.onCreate()

        AndroidLogcatLogger.installOnDebuggableApp(this)

        logcat { "starting application" }
    }
}
