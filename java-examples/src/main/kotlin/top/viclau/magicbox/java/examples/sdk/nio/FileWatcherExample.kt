/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.nio

import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent

/**
 * Source:
 * http://www.javaworld.com/javaworld/jw-10-2012/121016-maximize-java-nio-and-nio2-for-application-responsiveness.html?page=2
 */
fun main() {
    val thisDir = Paths.get(".")
    println("Now watching the current directory (${thisDir.toAbsolutePath()}) ...")

    try {
        val watcher = thisDir.fileSystem.newWatchService()
        thisDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE)

        val watchKey = watcher.take()
        val events = watchKey.pollEvents()
        for (event: WatchEvent<*> in events) {
            println("Someone just created the file '${event.context()}'.")
        }
    } catch (e: Exception) {
        println("Error: $e")
    }
}
