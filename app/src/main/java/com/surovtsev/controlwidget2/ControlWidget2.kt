package com.surovtsev.controlwidget2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.*
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding

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

class ControlWidget2Receiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ControlWidget2()
}
