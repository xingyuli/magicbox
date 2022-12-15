/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.ref

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference
import java.util.*


@Disabled("This is not a real test!")
class WeakReferenceTest {

    @Test
    fun testWeakReferenceUsingARuntimeConstantStringAsTheReferent() {
        val weakRef = WeakReference("aha")

        var strongRef = weakRef.get()
        assertNotNull(strongRef)

        // make the referent weakly reachable
        strongRef = null

        // As the referent string "aha" is placed in Permanent Generation /
        // Method Area's Runtime Constant Pool, the referent string "aha" will
        // not be reclaimed even if it is weakly reachable
        System.gc()

        assertNotNull(weakRef.get())
    }

    /* ********************************************************************* */
    @Test
    fun testLocalWeakReference() {
        // NOTE: still use a new String(...) as the referent
        val localEmail = WeakReference(String("ccc@ddd.com".toByteArray()))

        var localEmailRef = localEmail.get()
        assertNotNull(localEmailRef)

        // make localEmail's referent weakly reachable
        localEmailRef = null

        // localEmail's referent will be reclaimed
        System.gc()

        assertNull(localEmail.get())
    }

    @Test
    fun testWeakHashMap() {
        initCache()
        assertNotNull(userEmailToCostCenter["222@aaa.com"])

        System.gc()
        assertNull(userEmailToCostCenter["222@aaa.com"])
    }

    companion object {

        private val userEmailToCostCenter = WeakHashMap<String, String>()

        private fun initCache() {
            // as long as using new String(...) when add the keys, the entries
            // could be automatically reclaimed after GC
            userEmailToCostCenter[String("111@aaa.com".toByteArray())] = "111111"
            userEmailToCostCenter[String("222@aaa.com".toByteArray())] = "222222"
            userEmailToCostCenter[String("333@aaa.com".toByteArray())] = "333333"
        }

    }

}
