/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.kvsj

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.reflect.KClass

class JavaClassAndKClassTest {

    @Test
    fun test() {
        val kClass = JavaBean::class
        assertTrue(kClass is KClass)
        assertTrue(kClass.java is Class)

        val javaClass = JavaBean().javaClass
        assertTrue(javaClass is Class)
        assertTrue(javaClass.kotlin is KClass)
    }

}