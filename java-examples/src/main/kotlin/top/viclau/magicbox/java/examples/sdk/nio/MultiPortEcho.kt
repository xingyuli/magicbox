/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.nio

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import kotlin.system.exitProcess

/**
 * Source:
 * http://www.javaworld.com/javaworld/jw-10-2012/121016-maximize-java-nio-and-nio2-for-application-responsiveness.html?page=3
 */
class MultiPortEcho(private val ports: IntArray) {
    private val echoBuffer = ByteBuffer.allocate(1024)

    init {
        configureSelector()
    }

    private fun configureSelector() {
        // Create a new selector
        val selector = Selector.open()

        // Open a listener on each port, and register each one
        // with the selector
        for (i in ports.indices) {
            val ssc = ServerSocketChannel.open()
            ssc.configureBlocking(false)
            val ss = ssc.socket()
            val address = InetSocketAddress(ports[i])
            ss.bind(address)

            ssc.register(selector, SelectionKey.OP_ACCEPT)
            println("Going to listen on " + ports[i])
        }

        while (true) {
            selector.select()

            val iter = selector.selectedKeys().iterator()
            while (iter.hasNext()) {
                val key = iter.next()

                if (!key.isValid) {
                    continue
                }

                if (key.isAcceptable) {
                    // Accept the new connection
                    val ssc = key.channel() as ServerSocketChannel
                    val sc = ssc.accept()
                    sc.configureBlocking(false)

                    // Add the new connection to the selector
                    sc.register(selector, SelectionKey.OP_READ)
                    iter.remove()

                } else if (key.isReadable) {
                    // Read the data
                    val sc = key.channel() as SocketChannel

                    // Echo data
                    var bytesEchoed = 0
                    while (true) {
                        echoBuffer.clear()

                        val numberOfBytes = sc.read(echoBuffer)
                        if (-1 == numberOfBytes) {
                            break
                        }

                        echoBuffer.flip()
                        sc.write(echoBuffer)
                        bytesEchoed += numberOfBytes
                    }

                    println("Echoed $bytesEchoed from $sc")
                    sc.close()

                    iter.remove()
                }
            }
        }
    }

}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        System.err.println("Usage: java MultiPortEcho port [port port ...]")
        exitProcess(1)
    }

    val ports = IntArray(args.size)
    for (i in args.indices) {
        ports[i] = Integer.valueOf(args[i])
    }

    MultiPortEcho(ports)
}
