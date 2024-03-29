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

import kotlinx.coroutines.*


// TODO: remove file
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