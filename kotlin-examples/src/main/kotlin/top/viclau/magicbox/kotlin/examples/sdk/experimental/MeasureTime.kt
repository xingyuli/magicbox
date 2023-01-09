/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.sdk.experimental

import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main() {
    useClockMark()
    useMeasureTimedValue()
}

@ExperimentalTime
private fun useClockMark() {
    val mark = TimeSource.Monotonic.markNow() // might be inside the first function
    Thread.sleep(10)    // action
    println(mark.elapsedNow()) // might be inside the second function
}

@ExperimentalTime
private fun useMeasureTimedValue() {
    val (value, duration) = measureTimedValue {
        Thread.sleep(100)
        42
    }
    println(value)
    println(duration)
}
