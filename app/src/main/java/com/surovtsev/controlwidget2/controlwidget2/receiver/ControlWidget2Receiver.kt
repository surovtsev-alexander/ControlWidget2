package com.surovtsev.controlwidget2.controlwidget2.receiver

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase.ControlsInformationUseCase
import com.surovtsev.controlwidget2.controlwidget2.ControlWidget2
import com.surovtsev.controlwidget2.controlwidget2.helper.ControlWidget2Updater
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject


@AndroidEntryPoint
class ControlWidget2Receiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ControlWidget2()

    @Inject
    lateinit var controlsInformationUseCase: ControlsInformationUseCase

    @Inject
    lateinit var controlsInfoBroadcastReceiver: ControlsInfoBroadcastReceiver

    @Inject
    lateinit var controlWidget2Updater: ControlWidget2Updater

    private val coroutinesScope = MainScope()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        logcat { "onEnabled+" }

        val intent = Intent().apply {
            action = ControlsInfoBroadcastReceiver.INIT_ACTION
        }
        context.sendBroadcast(intent)

        logcat { "onEnabled-" }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        logcat { "onUpdate+" }
        logcat { "appWidgetIds: ${appWidgetIds.toList()}" }
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        coroutinesScope.launch {
            controlWidget2Updater.refreshState(
                glanceAppWidget
            )
        }

        logcat { "onUpdate-" }
    }


    override fun onDeleted(
        context: Context,
        appWidgetIds: IntArray,
    ) {
        super.onDeleted(context, appWidgetIds)

        logcat { "onDeleted" }
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val x: ControlWidget2Receiver = this
        logcat { "onReceive; this: ${System.identityHashCode(x)}"}
        logcat { "onReceive; intent: $intent" }
    }
}
