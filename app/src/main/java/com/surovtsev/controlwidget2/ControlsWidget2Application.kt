package com.surovtsev.controlwidget2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger

@HiltAndroidApp
class ControlsWidget2Application: Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidLogcatLogger.installOnDebuggableApp(this)
    }
}
