package top.viclau.magicbox.java.examples.junit.chapter8

import org.easymock.EasyMock.*
import org.easymock.EasyMockExtension
import org.easymock.Mock
import org.easymock.MockType
import org.easymock.TestSubject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@ExtendWith(EasyMockExtension::class)
class SampleServletEasyMockTest {

    @TestSubject
    private val servlet = SampleServlet()

    @Mock(MockType.STRICT)
    private lateinit var mockHttpServletRequest: HttpServletRequest

    @Mock(MockType.STRICT)
    private lateinit var mockHttpSession: HttpSession

    @Test
    fun testIsAuthenticatedAuthenticated() {
        // 1. define expectations
        expect(mockHttpServletRequest.getSession(eq(false))).andReturn(mockHttpSession)
        expect(mockHttpSession.getAttribute(eq("authenticated"))).andReturn("true")

        // 2. expectations done
        replay(mockHttpServletRequest)
        replay(mockHttpSession)

        // 3. execute and verify
        assertTrue(servlet.isAuthenticated(mockHttpServletRequest))
    }

    @Test
    fun testIsAuthenticatedNotAuthenticated() {
        expect(mockHttpServletRequest.getSession(eq(false))).andReturn(mockHttpSession)
        expect(mockHttpSession.getAttribute(eq("authenticated"))).andReturn("false")

        replay(mockHttpServletRequest)
        replay(mockHttpSession)

        assertFalse(servlet.isAuthenticated(mockHttpServletRequest))
    }

    @Test
    fun testIsAuthenticatedNoSession() {
        expect(mockHttpServletRequest.getSession(eq(false))).andReturn(null)

        replay(mockHttpServletRequest)
        replay(mockHttpSession)

        assertFalse(servlet.isAuthenticated(mockHttpServletRequest))
    }

    @AfterEach
    fun tearDown() {
        verify(mockHttpServletRequest)
        verify(mockHttpSession)
    }

}