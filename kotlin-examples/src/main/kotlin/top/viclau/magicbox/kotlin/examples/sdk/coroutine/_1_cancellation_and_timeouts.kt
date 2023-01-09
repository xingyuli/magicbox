/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.sdk.coroutine

import kotlinx.coroutines.*

fun main() = runBlocking {
//    cancellingCoroutineExecution()
//    cancellationIsCooperative()
//     makingComputationCodeCancellable()
//     closingResourcesWithFinally()
     timeout()
}

private suspend fun cancellingCoroutineExecution() {
    coroutineScope {
        val job = launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500)
            }
        }
        delay(1300)
        println("main: I'm tired of waiting!")
        job.cancel()
        job.join()
        println("main: Now I can quit.")
    }
}

// All the suspending functions in `kotlinx.coroutines` are cancellable. They
// check for cancellation of coroutine and throw `CancellationException` when
// cancelled. However, if a coroutine is working in a computation and does not
// check for cancellation, then it cannot be cancelled.
private suspend fun cancellationIsCooperative() {
    coroutineScope {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) { // computation loop, just wastes CPU
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300)
        println("main: I'm tired of waiting!")
        job.cancelAndJoin()
        println("main: Now I can quit.")
    }
}

private suspend fun makingComputationCodeCancellable() {
    coroutineScope {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (isActive) { // cancellable computation loop
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300)
        println("main: I'm tired of waiting!")
        job.cancelAndJoin()
        println("main: Now I can quit.")
    }
}

private suspend fun closingResourcesWithFinally() {
    coroutineScope {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500)
                }
            } finally {
                println("I'm running finally")
            }
        }
        delay(1300L)
        println("main: I'm tired of waiting!")
        job.cancelAndJoin()
        println("main: Now I can quit.")
    }
}

private suspend fun timeout() {
    withTimeout(1300) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500)
        }
    }
}
