/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.aqs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import top.viclau.magicbox.java.examples.sdk.aqs.CAS
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.collections.HashSet

class CASTest {

    @Test
    fun testGenerateTimestampConcurrently() {
        val concurrencyLevel = 100
        val prepared = CountDownLatch(concurrencyLevel)
        val go = CountDownLatch(1)
        val done = CountDownLatch(concurrencyLevel)
        val timestamps: MutableSet<Long> = Collections.synchronizedSet(HashSet<Long>())

        for (i in 0 until concurrencyLevel) {
            Thread {
                prepared.countDown()
                try {
                    go.await()
                } catch (e: InterruptedException) {
                    fail("unexpected interruption")
                }
                timestamps.add(CAS.getUniqueTimestamp())
                done.countDown()
            }.start()
        }

        try {
            prepared.await()
            go.countDown()
            done.await()
        } catch (e: InterruptedException) {
            fail("unexpected interruption")
        }

        assertEquals(concurrencyLevel, timestamps.size, "the count of timestamps not match")
    }

}