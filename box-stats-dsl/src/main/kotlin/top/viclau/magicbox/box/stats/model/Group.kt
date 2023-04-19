/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

class Group<DEST_TYPE>(bys: List<GroupBy<*, DEST_TYPE>>) {

    private var _bys: List<GroupBy<*, DEST_TYPE>> = bys

    fun add(by: GroupBy<*, DEST_TYPE>) {
        _bys += by
    }

    val bys: List<GroupBy<*, DEST_TYPE>> get() = _bys

}

class GroupBy<R : Any, DEST_TYPE>(
    val column: KProperty1<*, *>,
    val dimensionRecordMapper: (List<String>) -> List<R>,
    val dimensionValue: (R) -> Any?,
    extracts: List<GroupByExtract<R, *, DEST_TYPE>>
) {

    private var _extracts: List<GroupByExtract<R, *, DEST_TYPE>> = extracts

    fun add(extract: GroupByExtract<R, *, DEST_TYPE>) {
        _extracts += extract
    }

    val extracts: List<GroupByExtract<R, *, DEST_TYPE>> get() = _extracts

}

class GroupByExtract<R, R_PROP, DEST_TYPE>(
    private val src: (R) -> R_PROP?,
    private val alias: KMutableProperty1<DEST_TYPE, R_PROP?>
) {

    fun bind(dimensionRecord: R, target: DEST_TYPE) {
        val srcValue = src(dimensionRecord)
        alias.set(target, srcValue)
    }

}
