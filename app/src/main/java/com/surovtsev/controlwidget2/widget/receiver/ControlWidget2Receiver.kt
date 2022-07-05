package com.surovtsev.controlwidget2.widget.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.features.controlwidget2.domain.repository.ControlsInformationRepo
import com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase.ControlsInformationUseCase
import com.surovtsev.controlwidget2.widget.ControlWidget2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject


@AndroidEntryPoint
class ControlWidget2Receiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ControlWidget2()

    private val coroutineScope = MainScope()

    @Inject
    lateinit var controlsInformationUseCase: ControlsInformationUseCase

    @Inject
    lateinit var controlsInfoBroadcastReceiver: ControlsInfoBroadcastReceiver

    @Inject
    lateinit var controlsInformationRepo: ControlsInformationRepo

    private var updateJob: Job? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        logcat { "onUpdate+" }
        logcat { "appWidgetIds: ${appWidgetIds.toList()}" }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        logcat { "onUpdate-" }

        if (updateJob == null) {
            SupervisorJob().also {
                updateJob = it
                coroutineScope.launch(it) {
                    updateState(context)
                }
            }
        }
    }

    private suspend fun updateState(
        context: Context,
    ) {
        controlsInformationRepo.controlsInfoStateFlow.collectLatest { controlsInformation ->
            logcat { "updateState; controlsInformation: $controlsInformation" }

            val glanceIds = GlanceAppWidgetManager(context).getGlanceIds(ControlWidget2::class.java)

            glanceIds.map { glanceId ->
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                    prefs.toMutablePreferences().apply {
                        this[wifiState.key] = controlsInformation.wifiEnabled
                        this[bluetoothState.key] = controlsInformation.bluetoothEnabled
                        this[gpsState.key] = controlsInformation.gpsEnabled
                    }
                }
                glanceAppWidget.update(context, glanceId)
            }
        }
        logcat { "updateState: done" }
    }


    override fun onDeleted(
        context: Context,
        appWidgetIds: IntArray,
    ) {
        super.onDeleted(context, appWidgetIds)

    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        logcat { "onReceive; intent: $intent" }
    }

    companion object {
        data class KeyDescription(
            val key: Preferences.Key<Boolean>,
            val defValue: Boolean = false,
        ) {
            constructor(
                key: String,
                defValue: Boolean = false,
            ): this(
                booleanPreferencesKey(key),
                defValue,
            )

            fun getValueOrDefault(
                pref: Preferences
            ): Boolean {
                return pref[key] ?: defValue
            }
        }

        val wifiState = KeyDescription(
            "WIFI_STATE"
        )
        val bluetoothState = KeyDescription(
            "BLUETOOTH_STATE"
        )
        val gpsState = KeyDescription(
            "GPS_STATE"
        )
    }
}
