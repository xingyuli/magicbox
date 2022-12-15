/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.runtime

import java.util.*
import kotlin.system.exitProcess

fun main() {
    /*
     * This shutdown hook will be triggered when program exits normally, including:
     * (1) all non-daemon thread are finished or
     * (2) System.exit is called
     * or when Control-C send SIGINT signal.
     *
     * While the kill -9 will not.
     */
    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            Throwable("debug shutdown").printStackTrace()
        }
    })

    println("Please input something:")
    val `in` = Scanner(System.`in`)
    while (`in`.hasNextLine()) {
        val line = `in`.nextLine()
        if ("quit" == line) {
            break
        } else {
            println("continue ...")
        }
    }

    println("program exit")
    exitProcess(0)
}