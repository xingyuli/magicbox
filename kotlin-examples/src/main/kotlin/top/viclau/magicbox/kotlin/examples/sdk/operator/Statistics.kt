/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.sdk.operator

// see: http://blog.jetbrains.com/kotlin/2011/10/dsls-in-kotlin-part-1-whats-in-the-toolbox-builders/
data class Statistics(var failedNum: Int = 0, var succeededNum: Int = 0) {

    operator fun get(propName: String): Int = when (propName) {
        "failedNum" -> failedNum
        "succeededNum" -> succeededNum
        else -> throw IllegalArgumentException("unknown property: $propName")
    }

    operator fun set(propName: String, v: Int) {
        when (propName) {
            "failedNum" -> failedNum = v
            "succeededNum" -> succeededNum = v
        }
    }

    infix fun hasNo(propName: String): Boolean = get(propName) == 0

    fun ifNo(propName: String, f: () -> Unit) {
        if (hasNo(propName)) {
            f()
        }
    }

}