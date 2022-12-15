/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.exception

import java.io.PrintWriter
import java.io.StringWriter

private class MyHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        val stringWriter = StringWriter()
        e.printStackTrace(PrintWriter(stringWriter))

        System.err.println("Uncaught Exception thrown by $t, detail: ${stringWriter.buffer}")
    }
}

fun main() {
    Thread.setDefaultUncaughtExceptionHandler(MyHandler())

    // t1 use the default uncaught exception handler
    val t1: Thread = object : Thread() {
        override fun run() {
            throw NullPointerException("just for demonstration")
        }
    }
    t1.start()

    // t2 use self-defined uncaught exception handler
    val t2: Thread = object : Thread() {
        override fun run() {
            throw ArrayIndexOutOfBoundsException("exception in Runnable")
        }
    }
    t2.uncaughtExceptionHandler =
        Thread.UncaughtExceptionHandler { t, e -> System.err.println(e.javaClass.toString() + "[" + e.message + "] thrown by " + t) }
    t2.start()

    try {
        t1.join()
        t2.join()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    Thread.setDefaultUncaughtExceptionHandler(null)
    throw NumberFormatException("testing default handler of JVM")
}
