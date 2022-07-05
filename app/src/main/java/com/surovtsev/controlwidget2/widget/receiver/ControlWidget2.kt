package com.surovtsev.controlwidget2

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.*
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase.ControlsInformationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

class ControlWidget2: GlanceAppWidget() {

    @Composable
    override fun Content() {
        ControlWidget2Content(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(
                    day = Color.Blue,
                    night = Color.DarkGray,
                )
                .appWidgetBackground()
                .cornerRadius(16.dp)
                .padding(8.dp),
        )
    }

    companion object {
        const val CONTROL_WIDGET2_PREFS_KEY = "CONTROL_WIDGET2_PREFS_KEY"
        const val RECOMMENDED_DAILY_GLASSES = 8
        const val MAX_GLASSES = 999
    }
}


@AndroidEntryPoint
class ControlWidget2Receiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ControlWidget2()

    private val coroutineScope = MainScope()

    @Inject
    lateinit var controlsInformationUseCase: ControlsInformationUseCase

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
