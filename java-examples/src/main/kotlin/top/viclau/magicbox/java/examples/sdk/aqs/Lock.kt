/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.aqs

import java.util.concurrent.CountDownLatch

import java.util.concurrent.locks.ReentrantLock


private const val CONCURRENCY_LEVEL = 10

private val start = System.currentTimeMillis()
private val lock = ReentrantLock()

private val ready = CountDownLatch(CONCURRENCY_LEVEL)
private val go = CountDownLatch(1)
private val done = CountDownLatch(CONCURRENCY_LEVEL)

fun main() {
    for (i in 0 until CONCURRENCY_LEVEL) {
        spawnThread("thread-$i").start()
    }

    ready.await()
    go.countDown()
    done.await()
    println("main done")
}

private fun spawnThread(name: String): Thread {
    return Thread({
        ready.countDown()
        try {
            go.await()
        } catch (_: InterruptedException) {
        }

        println("${System.currentTimeMillis() - start} $name try lock")
        lock.lock()
        println("${System.currentTimeMillis() - start} lock acquired by $name")
        try {
            Thread.sleep(50)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            lock.unlock()
        }
        done.countDown()
    }, name)
}
