/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.sdk.coroutine.book

import kotlinx.coroutines.*

fun main() = runBlocking(CoroutineName("main")) {
    log("started")

    val v1 = async(CoroutineName("c1")) {
        delay(500)
        log("Running async")
        42
    }

    launch(CoroutineName("c2")) {
        delay(1000)
        log("Running launch")
    }

    log("The answer is ${v1.await()}")
}

fun CoroutineScope.log(msg: String) {
    val name = coroutineContext[CoroutineName]?.name
    println("[$name] $msg")
}