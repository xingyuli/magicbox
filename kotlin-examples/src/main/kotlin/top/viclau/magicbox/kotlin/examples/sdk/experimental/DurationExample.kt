/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.sdk.experimental

import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
suspend fun main() {
    greetAfterTimeout(100.milliseconds)
    greetAfterTimeout(1.seconds)
}

@ExperimentalTime
suspend fun greetAfterTimeout(duration: Duration) {
    delay(duration.inWholeMilliseconds)
    println("Hi!")
}
