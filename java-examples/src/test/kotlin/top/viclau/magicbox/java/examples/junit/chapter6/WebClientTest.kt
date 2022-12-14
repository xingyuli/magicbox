package top.viclau.magicbox.java.examples.junit.chapter6

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import spark.Spark
import java.net.URL


/**
 * Stub of the whole web server.
 */
class WebClientTest {

    @Test
    fun testGetContentOk() {
        val client = WebClient()
        val result = client.getContent(URL("http://localhost:4567/testGetContentOk"))
        assertEquals("It works", result)
    }

    @Test
    fun testGetContentNotFound() {
        val client = WebClient()
        val result = client.getContent(URL("http://localhost:4567/testGetContentNotFound"))
        assertNull(result)
    }

    companion object {

        @BeforeAll
        @JvmStatic
        fun setUp() {
            Spark.get("/testGetContentOk") { _, _ -> "It works" }
            Spark.get("/testGetContentNotFound") { _, _ -> Spark.halt(404) }
            Spark.awaitInitialization()
        }

        @AfterAll
        @JvmStatic
        fun clean() {
            Spark.stop()
        }

    }

}