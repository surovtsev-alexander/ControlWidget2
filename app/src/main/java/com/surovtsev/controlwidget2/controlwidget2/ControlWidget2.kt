package com.surovtsev.controlwidget2.controlwidget2

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import com.surovtsev.controlwidget2.controlwidget2.ui.ControlWidget2Content

class ControlWidget2: GlanceAppWidget() {

    @Composable
    override fun Content() {
        ControlWidget2Content(
            modifier = GlanceModifier
                .fillMaxSize()
                .appWidgetBackground()
                .padding(8.dp),
        )
    }

    companion object {
        const val CONTROL_WIDGET2_PREFS_KEY = "CONTROL_WIDGET2_PREFS_KEY"
        const val RECOMMENDED_DAILY_GLASSES = 8
        const val MAX_GLASSES = 999
    }
}