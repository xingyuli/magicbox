/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.proxy

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class ProxyTest {

    @Test
    fun testSimple() {
        val original: Shape = Triangle()

        val proxied = Proxy.newProxyInstance(
            original.javaClass.classLoader, original.javaClass.interfaces
        ) { proxy: Any?, method: Method, args: Array<Any?>? ->
            val retVal = if (args == null) {
                method.invoke(original)
            } else {
                method.invoke(original, *args)
            }

            if (method.name == "name") "$retVal enhanced" else retVal
        }

        assertEquals("San Jiao Xing enhanced", (proxied as Shape).name())
        assertEquals(0, proxied.version())
    }

    internal interface Shape {
        fun name(): String
        fun version(): Int
    }

    private class Triangle : Shape {

        private val version = 0

        override fun name(): String = "San Jiao Xing"

        override fun version(): Int = version

    }

}