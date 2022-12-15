/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.nio

import java.io.RandomAccessFile

private const val START: Long = 10
private const val SIZE: Long = 20

fun main() {
    val raf = RandomAccessFile("usefilelocks.txt", "rw")
    val fc = raf.channel

    println("trying to get lock")
    val lock = fc.lock(START, SIZE, false)
    println("got lock!")

    println("pausing")
    try {
        Thread.sleep(3000)
    } catch (_: InterruptedException) {
    }

    println("going to release lock")
    lock.release()
    println("released lock")

    raf.close()
}