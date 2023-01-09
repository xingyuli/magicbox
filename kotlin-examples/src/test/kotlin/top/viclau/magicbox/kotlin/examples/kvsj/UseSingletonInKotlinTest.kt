/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.kvsj

import org.junit.Assert.assertSame
import org.junit.Test

class UseSingletonInKotlinTest {

    @Test
    fun testCompanionSingleton() {
        val first = CompanionSingleton.instance
        val second = CompanionSingleton.instance
        assertSame(first, second)
    }

    @Test
    fun testStandardSingleton() {
        val first = StandardSingleton.getInstance()
        val second = StandardSingleton.getInstance()
        assertSame(first, second)
    }

    @Test
    fun testShorthandSingleton() {
        // only access INSTANCE triggers the initialization
        println(ShorthandSingleton::class)
        println(ShorthandSingleton) // print: do some preparation here

        val first = ShorthandSingleton
        val second = ShorthandSingleton
        assertSame(first, second)
    }

}