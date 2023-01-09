/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.concurrent

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DelayedTask(val name: String, val executor: ExecutorService, val delay: Long, val task: Runnable) : Runnable {

    private val createdTimeMillis = System.currentTimeMillis()

    override fun run() {
        if (System.currentTimeMillis() - createdTimeMillis < delay) {
            executor.submit(this)
            println("reschedule $name")
        } else {
            task.run()
            println("$name completed")
        }
    }

}

fun main() {
    val executor = Executors.newFixedThreadPool(2)
    for (i in 1..5) {
        executor.submit(DelayedTask("Task $i", executor, 2000, Runnable { println("Task $i doing...") }))
    }
    executor.awaitTermination(10, TimeUnit.SECONDS)
    executor.shutdown()
    println("program exit")
}