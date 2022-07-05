package com.surovtsev.controlwidget2.widget.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.surovtsev.controlwidget2.R
import com.surovtsev.controlwidget2.widget.ControlWidget2
import com.surovtsev.controlwidget2.widget.callback.AddWaterClickAction
import com.surovtsev.controlwidget2.widget.callback.ClearWaterClickAction

@Composable
fun ControlWidget2Content(
    modifier: GlanceModifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val prefs = currentState<Preferences>()
        val glassesOfWater = prefs[intPreferencesKey(ControlWidget2.CONTROL_WIDGET2_PREFS_KEY)] ?: 0
        ControlWidget2Counter(
            context =context,
            glassesOfWater =glassesOfWater,
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight()
        )
        ControlWidget2Goal(
            context = context,
            glassesOfWater = glassesOfWater,
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight()
        )
        ControlWidget2ButtonLayout(
            modifier = GlanceModifier
                .fillMaxSize()
                .defaultWeight()
        )
    }
}

@Composable
fun ControlWidget2Counter(
    context: Context,
    glassesOfWater: Int,
    modifier: GlanceModifier,
) {
    Text(
        text = context.getString(
            R.string.glasses_of_water_format,
            glassesOfWater
        ),
        modifier = modifier,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = ColorProvider(
                color = Color.White
            )
        ),
    )
}

@Composable
fun ControlWidget2Goal(
    context: Context,
    glassesOfWater: Int,
    modifier: GlanceModifier,
) {
    Text(
        text =
            when {
                glassesOfWater >= ControlWidget2.RECOMMENDED_DAILY_GLASSES ->
                    context.getString(
                        R.string.goal_met
                    )
                else ->
                    context.getString(
                        R.string.water_goal,
                        ControlWidget2.RECOMMENDED_DAILY_GLASSES
                    )
            },
        modifier = modifier,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = ColorProvider(
                color = Color.White
            )
        ),
    )
}

@Composable
fun ControlWidget2ButtonLayout(
    modifier : GlanceModifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            provider = ImageProvider(
                resId = R.drawable.id_baseline_delete_outline_24
            ),
            contentDescription = null,
            modifier = GlanceModifier
                .clickable(
                    onClick = actionRunCallback<ClearWaterClickAction>()
                )
                .defaultWeight()
        )
        Image(
            provider = ImageProvider(
                resId = R.drawable.ic_baseline_add_24
            ),
            contentDescription = null,
            modifier = GlanceModifier
                .clickable(
                    onClick = actionRunCallback<AddWaterClickAction>()
                )
                .defaultWeight()
        )
    }

}