/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.aqs

import java.util.concurrent.atomic.AtomicLong


object CAS {

    private val lastTimestamp = AtomicLong()

    fun getUniqueTimestamp(): Long {
        var latestTimestamp: Long
        while (true) {
            val oldLastTimestamp = lastTimestamp.get()
            latestTimestamp = System.currentTimeMillis()
            if (latestTimestamp != oldLastTimestamp && lastTimestamp.compareAndSet(oldLastTimestamp, latestTimestamp)) {
                break
            }
        }
        return latestTimestamp
    }

}