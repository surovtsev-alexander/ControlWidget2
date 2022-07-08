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


package com.surovtsev.controlwidget2.controlwidget2.helpers

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.surovtsev.controlwidget2.features.controlsinformation.domain.model.ControlsInformation
import com.surovtsev.controlwidget2.features.controlsinformation.domain.repository.ControlsInformationRepo
import com.surovtsev.controlwidget2.controlwidget2.ControlWidget2
import com.surovtsev.controlwidget2.commandsreceiver.CommandsReceiver.Companion.bluetoothStateKeyDescription
import com.surovtsev.controlwidget2.commandsreceiver.CommandsReceiver.Companion.gpsStateKeyDescription
import com.surovtsev.controlwidget2.commandsreceiver.CommandsReceiver.Companion.wifiStateKeyDescription
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ControlsInfoAndPreferencesBridge @Inject constructor(
    private val controlsInformationRepo: ControlsInformationRepo,
    @ApplicationContext private val context: Context,
) {
    private val coroutineScope = MainScope()

    init {
        coroutineScope.launch {
            updateStateLoop()
        }
    }

    private suspend fun updateStateLoop(
    ) {
        logcat { "starting: updateStateLoop" }
        controlsInformationRepo.controlsInfoStateFlow.collectLatest { controlsInformation ->
            val glanceAppWidget = ControlWidget2()
            logcat { "updateStateLoop; controlsInformation: $controlsInformation" }
            refreshStateHelper(
                glanceAppWidget,
                controlsInformation,
            )
        }
        logcat { "updateStateLoop: done" }
    }

    suspend fun refreshState(
        glanceAppWidget: GlanceAppWidget
    ) {
        refreshStateHelper(
            glanceAppWidget,
            controlsInformationRepo.controlsInfoStateFlow.value
        )
    }

    private suspend fun refreshStateHelper(
        glanceAppWidget: GlanceAppWidget,
        controlsInformation: ControlsInformation,
    ) {
        val glanceIds = GlanceAppWidgetManager(context).getGlanceIds(ControlWidget2::class.java)

        logcat { "refreshStateHelper; glaceIds: $glanceIds" }
        glanceIds.map { glanceId ->
            logcat { "updateState; glanceId: $glanceId" }
            updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[wifiStateKeyDescription.key] = controlsInformation.wifiEnabled
                    this[bluetoothStateKeyDescription.key] = controlsInformation.bluetoothEnabled
                    this[gpsStateKeyDescription.key] = controlsInformation.gpsEnabled
                }
            }
            glanceAppWidget.update(context, glanceId)
        }
    }
}