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


@Disabled("This is not a real test!")
class WeakReference_static_Test {

    @Test
    fun testStaticWeakReference() {
        var emailRef = staticEmail.get()
        assertNotNull(emailRef)

        // make email's referent weakly reachable
        emailRef = null

        // even though email is strongly reachable because of static variable
        // is in scope of GC Roots
        // email's referent will be reclaimed
        System.gc()

        assertNull(staticEmail.get())
    }

    companion object {

        // NOTE: the new String(...) is used as the referent, thus we allocate the
        // memory in the Java Heap rather than the Runtime Constant Pool
        @JvmStatic
        private val staticEmail = WeakReference(String("xxx@yyy.com".toByteArray()))

    }

}
