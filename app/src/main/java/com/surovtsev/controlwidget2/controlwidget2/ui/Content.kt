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


package com.surovtsev.controlwidget2.controlwidget2.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.surovtsev.controlwidget2.R
import com.surovtsev.controlwidget2.controlwidget2.ControlWidget2
import com.surovtsev.controlwidget2.controlwidget2.actions.AddWaterClickAction
import com.surovtsev.controlwidget2.controlwidget2.actions.ClearWaterClickAction
import com.surovtsev.controlwidget2.controlwidget2.actions.CommandToControlWidget2Action
import com.surovtsev.controlwidget2.commandsreceiver.CommandsReceiver.Companion.bluetoothStateKeyDescription
import com.surovtsev.controlwidget2.commandsreceiver.CommandsReceiver.Companion.gpsStateKeyDescription
import com.surovtsev.controlwidget2.commandsreceiver.CommandsReceiver.Companion.wifiStateKeyDescription

@Composable
fun ControlWidget2Content(
    modifier: GlanceModifier,
) {
    Row(
        modifier = modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val prefs = currentState<Preferences>()
        val wifiState = wifiStateKeyDescription.getValueOrDefault(prefs)
        val bluetoothState = bluetoothStateKeyDescription.getValueOrDefault(prefs)
        val gpsState = gpsStateKeyDescription.getValueOrDefault(prefs)

        val context = LocalContext.current

        ControlWidget2Button(
            rowScope = this,
            context = context,
            name = wifiStateKeyDescription.key.name,
            currState = wifiState,
            icons = listOf(R.drawable.wifi_on_icon, R.drawable.wifi_off_icon)
        )
        ControlWidget2Button(
            rowScope = this,
            context = context,
            name = bluetoothStateKeyDescription.key.name,
            currState = bluetoothState,
            icons = listOf(R.drawable.bluetooth_on_icon, R.drawable.bluetooth_off_icon)
        )
        ControlWidget2Button(
            rowScope = this,
            context = context,
            name = gpsStateKeyDescription.key.name,
            currState = gpsState,
            icons = listOf(R.drawable.gps_on_icon, R.drawable.gps_off_icon)
        )

//        val context = LocalContext.current
//        val glassesOfWater = prefs[intPreferencesKey(ControlWidget2.CONTROL_WIDGET2_PREFS_KEY)] ?: 0
//        ControlWidget2Counter(
//            context =context,
//            glassesOfWater =glassesOfWater,
//            modifier = GlanceModifier
//                .fillMaxWidth()
//                .defaultWeight()
//        )
//        ControlWidget2Goal(
//            context = context,
//            glassesOfWater = glassesOfWater,
//            modifier = GlanceModifier
//                .fillMaxWidth()
//                .defaultWeight()
//        )
//        ControlWidget2ButtonLayout(
//            modifier = GlanceModifier
//                .fillMaxSize()
//                .defaultWeight()
//        )
    }
}

@Composable
fun ControlWidget2Button(
    rowScope: RowScope,
    context: Context,
    name: String,
    currState: Boolean,
    icons: List<Int>
) {
    with(rowScope) {
        Image(
            modifier = GlanceModifier
                .fillMaxHeight()
                .defaultWeight()
                .clickable(
                    onClick = actionRunCallback<CommandToControlWidget2Action>(
                        actionParametersOf(
                            ActionParameters.Key<Boolean>(name) to !currState
                        )
                    )
                ),
            provider = ImageProvider(
                resId = if (currState) icons[0] else icons[1]
            ),
            contentDescription = null
        )
    }
}
