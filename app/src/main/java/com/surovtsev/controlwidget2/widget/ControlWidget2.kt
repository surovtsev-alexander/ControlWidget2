package com.surovtsev.controlwidget2.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import com.surovtsev.controlwidget2.widget.ui.ControlWidget2Content
import logcat.logcat

class ControlWidget2: GlanceAppWidget() {

    @Composable
    override fun Content() {
        ControlWidget2Content(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(
                    day = Color.Cyan,
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