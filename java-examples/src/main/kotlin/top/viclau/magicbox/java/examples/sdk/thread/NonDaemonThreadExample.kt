/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.thread

import java.util.concurrent.TimeUnit

fun main() {
    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            println("JVM exit")
        }
    })

    val t = Thread {
        println("task begin")
        try {
            TimeUnit.SECONDS.sleep(5)
            println("task done normally")
        } catch (e: InterruptedException) {
            println("task done abnormally")
        }
    }

    // A thread will use its parent thread's daemon setting, thus
    // thread t will be non-daemon as the main thread is non-daemon.
    t.start()

    // let the thread start its work
    try {
        TimeUnit.SECONDS.sleep(2)
    } catch (_: InterruptedException) {
    }
    println("main done")

    // And as the JVM only will exit when all non-daemon threads
    // have been finished, thus the thread t will finish the task
    // normally.

    /*
     * output would be:
     *   task begin
     *   main done
     *   task done normally
     *   JVM exit
     */
}
