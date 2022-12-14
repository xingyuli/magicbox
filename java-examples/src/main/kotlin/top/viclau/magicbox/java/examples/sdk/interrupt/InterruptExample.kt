package top.viclau.magicbox.java.examples.sdk.interrupt

import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

fun main() {
    val es = Executors.newSingleThreadExecutor()
    val task = FutureTask<Void> {
        val limit = 10
        for (i in 0 until limit) {
            if (Thread.interrupted()) {
                println("task has been INTERRUPTED, discard rest " + (limit - i) + " pieces of data")
                break
            }

            println("current: $i")

            try {
                TimeUnit.SECONDS.sleep(1)
            } catch (e: InterruptedException) {
                println("sleep interrupted")
                Thread.currentThread().interrupt()
            }
        }
        println("----------- task end -----------")
        null
    }
    es.execute(task)

    Thread {
        try {
            TimeUnit.SECONDS.sleep(3)
        } catch (_: InterruptedException) {
        }
        if (task.cancel(true)) {
            println("cancellation succeeded!")
        }
    }.start()

    es.shutdown()
}