/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.ref

import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*

private fun useWeakReferenceOnMap() {
    val map: MutableMap<Int, String> = HashMap()
    val weakRef: Reference<MutableMap<Int, String>> = WeakReference(map)

    var i = 0
    while (true) {
        val strongRef = weakRef.get()
        if (strongRef != null) {
            strongRef[i++] = "test$i"
            println("$i im still working!!!")
        } else {
            println("******* im free *******")
            break
        }
    }
}

private fun useWeakHashMap() {
    val cache: MutableMap<Int, String> = WeakHashMap()
    var i = 0
    while (true) {
        cache[i++] = "test$i"

        // you will notice that: i > cache.size
        println("$i im still working!!! size: ${cache.size}")
    }
}

fun main() {
    // useWeakReferenceOnMap()
    // useWeakHashMap()
}
