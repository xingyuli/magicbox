/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.nio.hsselect

import java.io.IOException
import java.net.Socket
import java.util.*

// Bounds on how much we write per cycle
private const val MIN_WRITE_SIZE = 1024
private const val MAX_WRITE_SIZE = 65536

// Bounds on how long we wait between cycles
private const val MIN_PAUSE = (0.05 * 1000).toInt()
private const val MAX_PAUSE = (0.5 * 1000).toInt()

/**
 * Source:
 * http://www.javaworld.com/jw-04-2003/jw-0411-select.html
 */
class Client(private val host: String, private val port: Int, numThreads: Int) : Runnable {

    // Random number generator
    private var rand = Random()

    init {
        for (i in 0 until numThreads) {
            Thread(this).start()
        }
    }

    override fun run() {
        val buffer = ByteArray(MAX_WRITE_SIZE)

        try {
            val s = Socket(host, port)

            val `in` = s.getInputStream()
            val out = s.getOutputStream()

            while (true) {
                val numToWrite = MIN_WRITE_SIZE + (rand.nextDouble() * (MAX_WRITE_SIZE - MIN_WRITE_SIZE)).toInt()

                for (i in 0 until numToWrite) {
                    buffer[i] = rand.nextInt(256).toByte()
                }

                out.write(buffer, 0, numToWrite)
                var sofar = 0
                while (sofar < numToWrite) {
                    sofar += `in`.read(buffer, sofar, numToWrite - sofar)
                }

                println(Thread.currentThread().toString() + " wrote " + numToWrite)

                val pause = MIN_PAUSE + (rand.nextDouble() * (MAX_PAUSE - MIN_PAUSE)).toInt()
                try {
                    Thread.sleep(pause.toLong())
                } catch (_: InterruptedException) {
                }
            }
        } catch (ie: IOException) {
            ie.printStackTrace()
        }
    }

}

fun main(args: Array<String>) {
    val host = args[0]
    val port = args[1].toInt()
    val numThreads = args[2].toInt()

    Client(host, port, numThreads)
}
