/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.ext

import top.viclau.magicbox.box.stats.dsl.GroupByBuilder
import top.viclau.magicbox.box.stats.dsl.GroupByExtractStringBuilder
import top.viclau.magicbox.box.stats.dsl.GroupStep
import top.viclau.magicbox.box.stats.dsl.WhereStep
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

fun <DEST_TYPE : Any> WhereStep<DEST_TYPE>.groupBy(
    by: KProperty1<*, *>,
    `as`: KMutableProperty1<DEST_TYPE, String?>
): GroupStep<DEST_TYPE> = groupBy(by) { `as`(`as`) }

fun <DEST_TYPE : Any> WhereStep<DEST_TYPE>.groupBy(
    by: KProperty1<*, *>,
    builder: GroupByExtractStringBuilder<DEST_TYPE>.Scope.() -> Unit
): GroupStep<DEST_TYPE> =
    groupBy(by, dimensionRecordMapper = { it }, dimensionValue = { it }) { extractString().builder() }

fun <R : Any, DEST_TYPE : Any> WhereStep<DEST_TYPE>.groupBy(
    by: KProperty1<*, *>,
    dimensionRecordMapper: (List<String>) -> List<R>,
    dimensionValue: (R) -> Any?,
    builder: GroupByBuilder<R, DEST_TYPE>.Scope.() -> Unit
): GroupStep<DEST_TYPE> = group { by(by, dimensionRecordMapper, dimensionValue, builder) }
