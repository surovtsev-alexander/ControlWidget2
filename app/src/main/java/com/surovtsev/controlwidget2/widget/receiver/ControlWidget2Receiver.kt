package com.surovtsev.controlwidget2.widget.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.surovtsev.controlwidget2.controlsinfobriadcastreceiver.ControlsInfoBroadcastReceiver
import com.surovtsev.controlwidget2.features.controlwidget2.domain.repository.ControlsInformationRepo
import com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase.ControlsInformationUseCase
import com.surovtsev.controlwidget2.widget.ControlWidget2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
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

    private val _coroutines = emptyMap<Int, Job>().toMutableMap()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        logcat { "onUpdate+" }
        logcat { "appWidgetIds: ${appWidgetIds.toList()}" }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        logcat { "onUpdate-" }

        appWidgetIds.map {
            if (!_coroutines.containsKey(it)) {
            }
        }

        coroutineScope.launch {
            observeData(context)
        }

        logcat {  "xx" }
        coroutineScope.launch {
            logcat { controlsInformationUseCase.getControlInformation().toString() }
        }
    }

    private suspend fun updateState(
        appWidgetId: Int
    ) {
        controlsInformationRepo.controlsInfoStateFlow.collect {

        }
    }


    override fun onDeleted(
        context: Context,
        appWidgetIds: IntArray,
    ) {
        super.onDeleted(context, appWidgetIds)

    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
    }

    private suspend fun observeData(
        context: Context,
    ) {

    }

    companion object {
        data class KeyDescription(
            val key: Preferences.Key<String>,
            val defValue: Boolean = false,
        ) {
            constructor(
                key: String,
                defValue: Boolean = false,
            ): this(
                stringPreferencesKey(key),
                defValue,
            )
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
