/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.junit.chapter6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.*

/**
 * Stub of connection.
 */
@Disabled("may pollute URL stream handling")
class WebClientTest1 {

    @Test
    fun testGetContentOk() {
        val client = WebClient()
        val result = client.getContent(URL("http://localhost"))
        assertEquals("It works", result)
    }

    private class StubStreamHandlerFactory : URLStreamHandlerFactory {
        override fun createURLStreamHandler(protocol: String): URLStreamHandler = StubHttpURLStreamHandler()
    }

    private class StubHttpURLStreamHandler : URLStreamHandler() {
        override fun openConnection(u: URL): URLConnection = StubHttpURLConnection(u)
    }

    private class StubHttpURLConnection(url: URL) : HttpURLConnection(url) {
        override fun getInputStream(): InputStream {
            if (!getDoInput()) {
                throw ProtocolException("Cannot read from URLConnection if doInput=false (call setDoInput(true))")
            }
            return ByteArrayInputStream("It works".toByteArray())
        }

        override fun connect() {
        }

        override fun disconnect() {}

        override fun usingProxy(): Boolean = false
    }

    companion object {
        @BeforeAll
        @JvmStatic
        fun setUp() {
            URL.setURLStreamHandlerFactory(StubStreamHandlerFactory())
        }
    }

}