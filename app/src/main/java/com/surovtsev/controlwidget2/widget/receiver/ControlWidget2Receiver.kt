package com.surovtsev.controlwidget2.widget.receiver

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.provider.Settings
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
import com.surovtsev.controlwidget2.widget.callback.CommandToControlWidget2Action
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

    @Inject
    lateinit var wifiManager: WifiManager

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    @Inject
    lateinit var locationManager: LocationManager

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

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        logcat { "onReceive; intent: $intent" }

        if (intent.action == CommandToControlWidget2Action.commandToControlWidget2) {
            val bundle = intent.extras

            bundle?.keySet()?.map { commandName ->
                val enable = bundle[commandName]?.run { this as Boolean }
                if (enable != null) {
                    when(commandName) {
                        wifiState.key.name -> {
                            wifiManager.isWifiEnabled = enable
                        }
                        bluetoothState.key.name -> {
//                            if (ActivityCompat.checkSelfPermission(context,
//                                    Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
//                            ) {
//                                // TODO: Consider calling
//                                //    ActivityCompat#requestPermissions
//                                // here to request the missing permissions, and then overriding
//                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                //                                          int[] grantResults)
//                                // to handle the case where the user grants the permission. See the documentation
//                                // for ActivityCompat#requestPermissions for more details.
//                                return
//                            }
                            if (enable) {
                                bluetoothAdapter.enable()
                            } else {
                                bluetoothAdapter.disable()
                            }
                        }
                        gpsState.key.name -> {
                            context.startActivity(
                                Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun convertImplicitIntentToExplicitIntent(implicitIntent: Intent, context: Context): Intent? {
        val pm = context.packageManager
        val resolveInfoList = pm.queryIntentServices(implicitIntent, 0)
        if (resolveInfoList == null || resolveInfoList.size != 1) {
            return null
        }
        val serviceInfo = resolveInfoList[0]
        val component = ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name)
        val explicitIntent = Intent(implicitIntent)
        explicitIntent.component = component
        return explicitIntent
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
                pref: Preferences,
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
