package com.surovtsev.controlwidget2.widget.ui

import kotlinx.coroutines.*

fun main() {
    val scope = CoroutineScope(Job() + Dispatchers.Default)
    scope.launch { test(scope, 0, 500, 1200) }
    scope.launch { test(scope, 1000, 500, 2000) }
    runBlocking {
        delay(5000)
    }
}

suspend fun test(
    scope: CoroutineScope,
    startCounter: Int,
    delayTime: Long,
    killTime: Long,
) {
    val j = scope.launch {
        for (i in 0..100) {
            println("i: ${i + startCounter}")
            delay(delayTime)
        }
    }
    delay(killTime)
    println("killing: $startCounter")
    j.cancel()
    println("killed: $startCounter")
}