package com.surovtsev.controlwidget2.controlwidget2.helper

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.surovtsev.controlwidget2.features.controlwidget2.domain.model.ControlsInformation
import com.surovtsev.controlwidget2.features.controlwidget2.domain.repository.ControlsInformationRepo
import com.surovtsev.controlwidget2.controlwidget2.ControlWidget2
import com.surovtsev.controlwidget2.controlwidget2.receiver.helpers.CommandsReceiver.Companion.bluetoothStateKeyDescription
import com.surovtsev.controlwidget2.controlwidget2.receiver.helpers.CommandsReceiver.Companion.gpsStateKeyDescription
import com.surovtsev.controlwidget2.controlwidget2.receiver.helpers.CommandsReceiver.Companion.wifiStateKeyDescription
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ControlWidget2Updater @Inject constructor(
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