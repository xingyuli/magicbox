/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.junit.chapter7

import org.easymock.EasyMock.*
import org.easymock.EasyMockExtension
import org.easymock.Mock
import org.easymock.MockType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import java.io.InputStream

@ExtendWith(EasyMockExtension::class)
class WebClientEasyMockTest {

    @Mock(MockType.STRICT)
    private lateinit var factory: ConnectionFactory

    @Mock(MockType.STRICT)
    private lateinit var stream: InputStream

    @Test
    fun testGetContentOk() {
        // define expectations
        expect(factory.getData()).andReturn(stream)
        expect(stream.read()).andReturn('W'.code)
        expect(stream.read()).andReturn('o'.code)
        expect(stream.read()).andReturn('r'.code)
        expect(stream.read()).andReturn('k'.code)
        expect(stream.read()).andReturn('s'.code)
        expect(stream.read()).andReturn('!'.code)
        expect(stream.read()).andReturn(-1)
        stream.close()

        // expectations done
        replay(factory)
        replay(stream)

        // execute
        val client = WebClient()
        val result = client.getContent(factory)

        // verify
        assertEquals("Works!", result)
    }

    @Test
    fun testGetContentCannotCloseInputStream() {
        expect(factory.getData()).andReturn(stream)
        expect(stream.read()).andReturn(-1)
        stream.close()
        expectLastCall<Any>().andThrow(IOException("cannot close"))

        replay(factory)
        replay(stream)

        val client = WebClient()
        val result = client.getContent(factory)

        assertNull(result)
    }

    @AfterEach
    fun tearDown() {
        verify(factory)
        verify(stream)
    }

}