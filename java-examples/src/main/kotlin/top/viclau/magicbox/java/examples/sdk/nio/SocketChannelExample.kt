package top.viclau.magicbox.java.examples.sdk.nio

import java.io.IOException
import java.net.InetSocketAddress
import java.nio.CharBuffer
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.charset.Charset
import java.util.concurrent.Executors

private class IOWorker : Runnable {
    override fun run() {
        try {
            val channel = ServerSocketChannel.open()
            channel.configureBlocking(false)
            val socket = channel.socket()
            socket.bind(InetSocketAddress("localhost", 10800))

            val selector = Selector.open()
            channel.register(selector, channel.validOps())

            while (true) {
                selector.select()

                println("got it")

                val iter = selector.selectedKeys().iterator()
                while (iter.hasNext()) {
                    val key = iter.next()
                    iter.remove()

                    if (!key.isValid) {
                        continue
                    }

                    if (key.isAcceptable) {
                        println("accept")

                        val ssc = key.channel() as ServerSocketChannel
                        val sc = ssc.accept()
                        sc.configureBlocking(false)
                        sc.register(selector, sc.validOps())
                    }

                    if (key.isWritable) {
                        println("write")

                        val client = key.channel() as SocketChannel

                        val charset = Charset.forName("UTF-8")
                        val encoder = charset.newEncoder()
                        val charBuffer = CharBuffer.allocate(32)
                        charBuffer.put("Hello World")
                        charBuffer.flip()

                        val content = encoder.encode(charBuffer)
                        client.write(content)
                        key.cancel()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun main() {
    Executors.newSingleThreadExecutor().submit(IOWorker()).get()
}
