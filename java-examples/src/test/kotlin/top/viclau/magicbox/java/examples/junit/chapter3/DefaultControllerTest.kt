/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.junit.chapter3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

class DefaultControllerTest {

    private lateinit var controller: DefaultController
    private lateinit var req: Request
    private lateinit var handler: RequestHandler

    @BeforeEach
    fun instantiate() {
        controller = DefaultController()
        req = SampleRequest()
        handler = SampleHandler()

        controller.addHandler(req, handler)
    }

    @Test
    fun testAddHandler() {
        val actualHandler = controller.getHandler(req)
        assertEquals(handler, actualHandler, "Handler we set in controller should be the same handler we get")
    }

    @Test
    fun testProcessRequest() {
        val resp = controller.processRequest(req)
        assertNotNull(resp, "Must not return a null response")
        assertEquals(SampleResponse(), resp)
    }

    @Test
    fun testProcessRequestAnswersErrorResponse() {
        val req = SampleRequest("testError")
        val handler = SampleExceptionHandler()
        controller.addHandler(req, handler)

        val resp = controller.processRequest(req)
        assertNotNull(resp, "Must not return a null response")
        assertEquals(ErrorResponse::class, resp::class)
    }

    @Test
    fun testGetHandlerNotDefined() {
        assertThrows(RuntimeException::class.java) {
            controller.getHandler(SampleRequest("testNotDefined"))
        }
    }

    @Test
    fun testAddRequestDuplicateName() {
        assertThrows(RuntimeException::class.java) {
            controller.addHandler(SampleRequest(), SampleHandler())
        }
    }

    @Test
    @Timeout(value = 130, unit = TimeUnit.MILLISECONDS)
    @Disabled("Ignore for now until we decide a decent time-limit")
    fun testProcessMultipleRequestsTimeout() {
        val handler: RequestHandler = SampleHandler()
        for (i in 0..99998) {
            val req: Request = SampleRequest(i.toString())
            controller.addHandler(req, handler)
            val resp = controller.processRequest(req)
            assertNotNull(resp)
            assertNotEquals(ErrorResponse::class, resp::class)
        }
    }

    private class SampleRequest(override val name: String = "Test") : Request

    private class SampleHandler : RequestHandler {
        override fun process(req: Request): Response = SampleResponse()
    }

    private class SampleExceptionHandler : RequestHandler {
        override fun process(req: Request): Response {
            throw Exception("error processing request")
        }
    }

    private class SampleResponse : Response {

        override val name: String
            get() = "Test"

        override fun equals(other: Any?): Boolean = other is SampleResponse && name == other.name

        override fun hashCode(): Int = name.hashCode()

    }

}