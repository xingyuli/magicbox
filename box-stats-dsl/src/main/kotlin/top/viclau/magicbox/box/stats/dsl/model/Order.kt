/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.model

import kotlin.reflect.KProperty1

enum class Direction(val intValue: Int) {

    ASC(1), DESC(0);

    companion object {
        fun from(intValue: Int?, defaultDir: Direction): Direction {
            if (intValue == null) {
                return defaultDir
            }
            return values().firstOrNull { it.intValue == intValue } ?: defaultDir
        }
    }

}

class Order(private val by: By, private val dir: Direction) {

    sealed interface By {
        val propName: String
    }

    class ByProp<DEST_TYPE>(private val prop: KProperty1<DEST_TYPE, Comparable<*>?>) : By {

        override val propName: String get() = prop.name

    }

    class ByPropName(override val propName: String) : By

}
