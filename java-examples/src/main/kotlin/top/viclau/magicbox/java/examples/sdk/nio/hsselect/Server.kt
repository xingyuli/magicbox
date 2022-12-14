package top.viclau.magicbox.java.examples.sdk.nio.hsselect

import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

/**
 * Source:
 * http://www.javaworld.com/jw-04-2003/jw-0411-select.html
 */
class Server(
    // The port we will listen on
    private val port: Int
) : Runnable {

    // A pre-allocated buffer for encrypting data
    private val buffer = ByteBuffer.allocate(16384)

    init {
        Thread(this).start()
    }

    override fun run() {
        try {
            // Instead of creating a ServerSocket,
            // create a ServerSocketChannel
            val ssc = ServerSocketChannel.open()

            // Get the Socket connected to this channel, and bind it to the
            // listening port
            val isa = InetSocketAddress(port)
            val ss = ssc.socket()
            ss.bind(isa)

            // Set it to non-blocking, so we can use select
            ssc.configureBlocking(false)

            // Create a new Selector for selecting
            val selector = Selector.open()

            // Register the ServerSocketChannel, so we can listen for incoming
            // connections
            ssc.register(selector, SelectionKey.OP_ACCEPT)
            println("Listening on port $port")

            while (true) {
                // See if we've had any activity -- either an incoming
                // connection, or incoming data on an existing connection
                val num = selector.select()

                // If we don't have any activity, loop around and wait again
                if (num == 0) {
                    continue
                }

                // Get the keys corresponding to the activity that has been
                // detected, and process them one by one
                val keys = selector.selectedKeys()
                val iter: Iterator<SelectionKey> = keys.iterator()
                while (iter.hasNext()) {
                    // Get a key representing one of bits of I/O activity
                    val key = iter.next()

                    // What kind of activity is it?
                    if (key.isAcceptable) {
                        // It's an incoming connection.
                        // Register this socket with the selector so we can
                        // listen for input on it

                        val s = ss.accept()
                        println("Got connection from $s")

                        // Make sure to make in non-blocking, so we can use a
                        // selector on it.
                        val sc = s.channel
                        sc.configureBlocking(false)

                        // Register it with the selector, for reading
                        sc.register(selector, SelectionKey.OP_READ)

                    } else if (key.isReadable) {
                        // Its incoming data on a connection, so process it
                        val sc = key.channel() as SocketChannel
                        try {
                            val ok = processInput(sc)

                            // If the connection is dead, then remove it from
                            // the selector and close it
                            if (!ok) {
                                key.cancel()

                                val s = sc.socket()
                                try {
                                    s.close()
                                } catch (ie: IOException) {
                                    System.err.println("Error closing socket $s: $ie")
                                }
                            }

                        } catch (ie: IOException) {
                            // On exception, remove this channel from the
                            // selector
                            key.cancel()
                            try {
                                sc.close()
                            } catch (ie2: IOException) {
                                println(ie2)
                            }
                            println("Closed $sc")
                        }
                    }
                }

                // We remove the selected keys, because we've dealt with them.
                keys.clear()
            }
        } catch (ie: IOException) {
            System.err.println(ie)
        }
    }

    // Do some cheesy encryption on the incoming data, and send it back out.
    private fun processInput(sc: SocketChannel): Boolean {
        buffer.clear()
        sc.read(buffer)
        buffer.flip()

        // If no data, close the connection
        if (buffer.limit() == 0) {
            return false
        }

        // Simple rot-13 encryption
        for (i in 0 until buffer.limit()) {
            var b: Byte = buffer[i]
            if (b >= 'a'.code.toByte() && b <= 'm'.code.toByte() || b >= 'A'.code.toByte() || b <= 'M'.code.toByte()) {
                b = b.plus(13).toByte()
            } else if (b >= 'n'.code.toByte() && b <= 'z'.code.toByte() || b >= 'N'.code.toByte() && b <= 'Z'.code.toByte()) {
                b = b.minus(13).toByte()
            }
            buffer.put(i, b)
        }

        // buffer.get(int, byte) and buffer.put(int, byte) will not impact
        // the limit

        sc.write(buffer)

        println("Processed ${buffer.limit()} from $sc")

        return true
    }

}

fun main(args: Array<String>) {
    val port = args[0].toInt()
    Server(port)
}
