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
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

/**
 * Source:
 * http://blog.yohanliyanage.com/2010/10/ktjs-3-soft-weak-phantom-references/
 * <p>
 *
 * On Oracle(Sun)'s HotSpot JVM:<br>
 * Strong > Soft > Weak > Phantom<br>
 *
 * This is a demonstration on Oracle(Sun)'s JVM implementation which means
 * might not work for some other JVM implementors.
 */
@Disabled("This is not a real test!")
class ReferenceTest {

    /**
     * 1. Soft references are most often used to implement memory-sensitive
     *    caches.
     * 2. All soft references to softly-reachable objects are guaranteed to
     *    have been cleared before the virtual machine throws an
     *    OutOfMemoryError.
     */
    @Test
    fun testSoftRef() {
        // Make a Soft reference
        val softRef = SoftReference(Any())

        // Get a strong reference, and make it eligible for GC !
        var obj = softRef.get()
        obj = null

        System.gc()

        // since JVM has enough memory, it didn't reclaim the memory
        // consumed by our softly referenced instance
        assertNotNull(softRef.get())
    }


    /**
     * 1. Weak references are most often used to implement canonicalizing
     *    mappings.
     * 2. Unlike SoftReference, Weak references can be reclaimed by the JVM
     *    during a GC cycle, even though there's enough free memory available.
     */
    @Test
    fun testWeakRef() {
        // Make a Weak reference
        val weakRef = WeakReference(Any())

        // as long as the GC does not occur, we can retrieve a strong reference
        // out of a weak reference by calling the ref.get() method
        assertNotNull(weakRef.get())

        // Get a strong reference again. Now it's not eligible for GC
        var strongRef = weakRef.get()
        System.gc()

        // as we still hold a strong reference, thus it's not weak-reachable,
        // the GC will not reclaim our weak reference
        assertNotNull(weakRef.get())

        // Make the instance eligible for GC again
        strongRef = null
        System.gc()

        // as the obj instance is weak reachable, JVM reclaim the memory of that
        // instance
        assertNull(weakRef.get())
    }

}