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

    // run as daemon
    t.isDaemon = true
    t.start()

    // let the thread start its work
    try {
        TimeUnit.SECONDS.sleep(2)
    } catch (_: InterruptedException) {
    }
    println("main done")

    // as the main thread has been finished, and only non-daemon threads
    // remaining, JVM will exit which means the thread t will not have
    // its task done

    /*
     * output would be:
     *   task begin
     *   main done
     *   JVM exit
     */
}