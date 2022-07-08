/*
MIT License

Copyright (c) [2022] [Alexander Surovtsev]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */


package com.surovtsev.controlwidget2.controlwidget2.actions

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.surovtsev.controlwidget2.controlwidget2.ControlWidget2
import com.surovtsev.controlwidget2.controlwidget2.receiver.WidgetReceiver
import com.surovtsev.controlwidget2.commandsreceiver.CommandsReceiver.Companion.COMMAND_FROM_UI
import logcat.logcat


class CommandToControlWidget2Action(
): ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        logcat { "onRun; glanceId: $glanceId" }

        val intent = Intent().apply {
            action = COMMAND_FROM_UI
            parameters.asMap().map { (key, value) ->
                putExtra(key.toString(), value as Boolean)
            }
        }
        context.sendBroadcast(intent)
    }
}

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
        logcat { "onRun" }
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

class Refresh: ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        logcat { "onRun" }
        val intent =Intent(
            context,
            WidgetReceiver::class.java
        ).apply {
            action = UPDATE_ACTION
        }

        context.sendBroadcast(intent)
    }

    companion object {
        const val UPDATE_ACTION ="updateAction"
    }
}