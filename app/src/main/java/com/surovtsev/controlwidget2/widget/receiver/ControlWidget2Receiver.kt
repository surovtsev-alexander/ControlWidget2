package com.surovtsev.controlwidget2.widget.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase.ControlsInformationUseCase
import com.surovtsev.controlwidget2.widget.ControlWidget2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject


@AndroidEntryPoint
class ControlWidget2Receiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ControlWidget2()

    private val coroutineScope = MainScope()

    @Inject
    lateinit var controlsInformationUseCase: ControlsInformationUseCase

    @Inject
    lateinit var controlsInfoBroadcastReceiver: ControlsInfoBroadcastReceiver

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        coroutineScope.launch {
            observeData(context)
        }

        logcat {  "xx" }
        coroutineScope.launch {
            logcat { controlsInformationUseCase.getControlInformation().toString() }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        logcat { "onReceive; intent: $intent" }
        coroutineScope.launch {
            observeData(context)
        }
    }

    private suspend fun observeData(
        context: Context,
    ) {

    }
}
