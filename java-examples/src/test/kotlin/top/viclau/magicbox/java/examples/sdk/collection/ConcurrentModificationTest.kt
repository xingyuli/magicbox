/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.collection

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConcurrentModificationTest {

    private val list: MutableList<String> = ArrayList()

    @BeforeEach
    fun prepare() {
        list.add("one")
        list.add("two")
        list.add("three")
    }

    @AfterEach
    fun tearDown() {
        list.clear()
    }

    @Test
    fun testModificationWithinSameThread() {
        assertThrows(ConcurrentModificationException::class.java) {
            val iter: Iterator<String> = list.iterator()
            list.add("four")
            iter.next()
        }
    }

}