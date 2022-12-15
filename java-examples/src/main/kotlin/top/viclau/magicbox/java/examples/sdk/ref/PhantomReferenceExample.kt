/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.ref

import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.util.concurrent.TimeUnit

fun main() {
    val queue = ReferenceQueue<Any>()
    val phantomRef = PhantomReference(Any(), queue)

    Thread {
        try {
            println("Awaiting for GC")
            // This will block till it is GCd
            val ref = queue.remove() as PhantomReference<*>
            println(ref)
            println("Referenced GC'd")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }.start()

    // Wait for 2nd thread to start
    TimeUnit.SECONDS.sleep(2)

    println("Invoking GC")
    System.gc()
}
