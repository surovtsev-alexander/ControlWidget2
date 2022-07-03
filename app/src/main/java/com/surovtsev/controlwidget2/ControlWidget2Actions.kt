package com.surovtsev.controlwidget2

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition


class AddWaterClickAction : ActionCallback {
    override suspend fun onRun(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) {
            it.toMutablePreferences()
                .apply {
                    val glassesOfWater =
                        this[intPreferencesKey(
                            ControlWidget2.CONTROL_WIDGET2_PREFS_KEY
                        )] ?: 0
                    if (glassesOfWater < ControlWidget2.MAX_GLASSES) {
                        this[intPreferencesKey(
                            ControlWidget2.CONTROL_WIDGET2_PREFS_KEY
                        )] = glassesOfWater + 1
                    }
                }
        }
        ControlWidget2().update(context, glanceId)
    }
}

class ClearWaterClickAction: ActionCallback {
    override suspend fun onRun(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
       updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) {
           it.toMutablePreferences()
               .apply {
                   this[intPreferencesKey(
                       ControlWidget2.CONTROL_WIDGET2_PREFS_KEY
                   )] = 0
               }
       }
        ControlWidget2().update(context, glanceId)
    }
}