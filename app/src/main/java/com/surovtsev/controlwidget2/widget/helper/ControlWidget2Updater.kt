package com.surovtsev.controlwidget2.widget.helper

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.surovtsev.controlwidget2.features.controlwidget2.domain.model.ControlsInformation
import com.surovtsev.controlwidget2.features.controlwidget2.domain.repository.ControlsInformationRepo
import com.surovtsev.controlwidget2.widget.ControlWidget2
import com.surovtsev.controlwidget2.widget.receiver.ControlWidget2Receiver
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
        controlsInformationRepo.controlsInfoStateFlow.collectLatest { controlsInformation ->
            val glanceAppWidget = ControlWidget2()
            logcat { "updateStateLoop; controlsInformation: $controlsInformation" }
//            logcat { "updateJob; state: ${ControlWidget2Receiver.updateJob?.isActive}; ${ControlWidget2Receiver.updateJob?.isCancelled}; ${ControlWidget2Receiver.updateJob?.isCompleted}" }
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

        glanceIds.map { glanceId ->
            logcat { "updateState; glanceId: $glanceId" }
            updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[ControlWidget2Receiver.wifiState.key] = controlsInformation.wifiEnabled
                    this[ControlWidget2Receiver.bluetoothState.key] = controlsInformation.bluetoothEnabled
                    this[ControlWidget2Receiver.gpsState.key] = controlsInformation.gpsEnabled
                }
            }
            glanceAppWidget.update(context, glanceId)
        }
    }
}