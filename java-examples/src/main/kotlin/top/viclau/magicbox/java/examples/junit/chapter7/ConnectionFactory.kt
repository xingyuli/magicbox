package top.viclau.magicbox.java.examples.junit.chapter7

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

interface ConnectionFactory {
    @Throws(IOException::class)
    fun getData(): InputStream
}

class HttpURLConnectionFactory(private val url: URL) : ConnectionFactory {
    override fun getData(): InputStream {
        val connection = url.openConnection() as HttpURLConnection
        return connection.inputStream
    }
}