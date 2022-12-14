package top.viclau.magicbox.java.examples.junit.chapter7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.InputStream

class WebClientManualMockTest {

    @Test
    fun testGetContentOk() {
        // 1. preparation
        val mockConnectionFactory = MockConnectionFactory()
        val mockStream = MockInputStream()
        mockStream.setBuffer("It works")
        mockConnectionFactory.setData(mockStream)

        // 2. execute
        val client = WebClient()
        val result = client.getContent(mockConnectionFactory)

        // 3. verify
        assertEquals("It works", result)
        mockStream.verify()
    }

    private class MockConnectionFactory : ConnectionFactory {

        private lateinit var inputStream: InputStream

        fun setData(inputStream: InputStream) {
            this.inputStream = inputStream
        }

        override fun getData(): InputStream {
            return inputStream
        }

    }

    private class MockInputStream : InputStream() {

        private lateinit var buffer: String
        private var position = 0
        private var closeCount = 0

        fun setBuffer(buffer: String) {
            this.buffer = buffer
        }

        override fun read(): Int = if (position == buffer.length) {
            -1
        } else buffer[position++].code

        override fun close() {
            closeCount++
            super.close()
        }

        fun verify() {
            if (closeCount != 1) {
                throw AssertionError(
                    "close() should have been called once and once only"
                )
            }
        }

    }

}
