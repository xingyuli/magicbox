/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.sdk.operator

import org.junit.Assert.*
import org.junit.Test

class ExtTest {

    @Test
    fun testBuiltInOverloadedPlusSign() {
        val arrayA = arrayOf("a", "b")
        val arrayB = arrayOf("c", "d")

        // "a + b" equivalents to "a.plus(b)"
        assertArrayEquals(arrayOf("a", "b", "c", "d"), arrayA + arrayB)
    }

    @Test
    fun testMyOverloadedPlusSign() {
        val statA = Statistics(2, 8)
        val statB = Statistics(1, 19)
        assertEquals("Statistics(failedNum=3, succeededNum=27)", (statA + statB).toString())
    }

    @Test
    fun testBuiltInOverloadedIndexation() {
        val o = ArrayList<Int>()
        o.addAll(1..10)

        // "o[something]" equivalents to "o.get(something)"
        assertEquals(3, o[2])

        // "o[something] = v" equivalents to "o.set(something, v)"
        o[2] = 99
        assertEquals(99, o[2])

        // NOTE: `something` is not limited to integrals!
    }

    @Test
    fun testMyOverloadedIndexation() {
        val stat = Statistics(0, 10)
        stat["failedNum"] = 1
        stat["succeededNum"] = 9
        assertEquals("Statistics(failedNum=1, succeededNum=9)", stat.toString())

        try {
            stat["something"]
            fail("exception expected")
        } catch (e: IllegalArgumentException) {
            assertEquals("unknown property: something", e.message)
        }
    }

    @Test
    fun testInfixFunctionCall() {
        // i.e., fluent api
        // stat hasNo "failedNum"    equivalent to    stat.hasNo("failedNum")
        assertEquals(true, Statistics(0, 10) hasNo "failedNum")
    }

    @Test
    fun testFunctionLiterals() {
        // i.e., closure

        var result = ""

        // Convention:
        // you can pass the last function literal outside the parentheses you
        // put around your argument list
        Statistics(0, 10).ifNo("failedNum") {
            result = "closure executed"
        }
        assertEquals("closure executed", result)

        // Convention:
        // If a function with only one parameter is expected, the parameter
        // declaration may be omitted, and the default name 'it' will be used.
        // i.e., "filter { it.foo() }" is the same as "filter { it => it.foo() }"
    }

    @Test
    fun testExtensionLambda() {
        withUser(User("Vic", "Lau")) {
            println("This is the first time I($firstName) play with the app.")
        }
    }

}