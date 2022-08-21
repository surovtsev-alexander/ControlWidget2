package com.surovtsev.controlwidget2.screenbroadcastreceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import logcat.logcat

class ScreenBroadcastReceiver(
): BroadcastReceiver() {

    fun registerReceiver(
        context: Context,
    ) {
        val actions = listOf(
            "android.intent.action.BOOT_COMPLETED",
            "android.intent.action.SCREEN_ON",
            "android.intent.action.SCREEN_OFF",
        )
        val intentFilter = IntentFilter().apply {
            actions.map {
                addAction(it)
            }
        }
        context
            .registerReceiver(
                this,
                intentFilter
            )
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(p0: Context?, p1: Intent?) {
        logcat { "onReceive: $p1" }

        if (p0 != null && p1 != null) {
            logcat { "show toast" }
            Toast
                .makeText(
                    p0,
                    "AA",
                    Toast.LENGTH_LONG
                ).show()
        }
    }
}