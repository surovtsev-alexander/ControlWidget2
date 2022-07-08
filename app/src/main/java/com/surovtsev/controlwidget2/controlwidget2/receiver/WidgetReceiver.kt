package com.surovtsev.controlwidget2.controlwidget2.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.controlwidget2.ControlWidget2
import com.surovtsev.controlwidget2.controlwidget2.helpers.ControlsInfoAndPreferencesBridge
import com.surovtsev.controlwidget2.features.controlsinformation.domain.usecase.ControlsInformationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject


@AndroidEntryPoint
class WidgetReceiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ControlWidget2()

    @Inject
    lateinit var controlsInformationUseCase: ControlsInformationUseCase

    @Inject
    lateinit var controlsInfoBroadcastReceiver: ControlsInfoBroadcastReceiver

    @Inject
    lateinit var controlsInfoAndPreferencesBridge: ControlsInfoAndPreferencesBridge

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

        refreshUIState()

        logcat { "onUpdate-" }
    }


    override fun onDeleted(
        context: Context,
        appWidgetIds: IntArray,
    ) {
        super.onDeleted(context, appWidgetIds)

        logcat { "onDeleted" }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val x: WidgetReceiver = this
        logcat { "onReceive; this: ${System.identityHashCode(x)}"}
        logcat { "onReceive; intent: $intent" }
    }

    private fun refreshUIState() {
        coroutinesScope.launch {
            controlsInfoAndPreferencesBridge.refreshState(
                glanceAppWidget
            )
        }
    }
}
