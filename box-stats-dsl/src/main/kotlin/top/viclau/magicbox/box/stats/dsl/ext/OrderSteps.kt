/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.ext

import top.viclau.magicbox.box.stats.dsl.GroupStep
import top.viclau.magicbox.box.stats.dsl.OrderStep
import top.viclau.magicbox.box.stats.dsl.model.Direction
import kotlin.reflect.KProperty1

fun <DEST_TYPE : Any> GroupStep<DEST_TYPE>.orderBy(
    by: KProperty1<DEST_TYPE, Comparable<*>?>,
    dir: Direction
): OrderStep<DEST_TYPE> = order { by(by, dir) }

fun <DEST_TYPE : Any> GroupStep<DEST_TYPE>.orderBy(by: String, dir: Direction): OrderStep<DEST_TYPE> =
    order { by(by, dir) }
