/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.model.superset.chart.data


data class ChartDataFilter(
    val col: String,

    // supported operations by superset version 1.1.0:
    // ==, !=, >, <, >=, <=, LIKE, IS NULL, IS NOT NULL, IN, NOT IN, REGEX
    val op: String,
    val `val`: Any?
) {

    companion object {

        fun eq(col: String, `val`: Any) = ChartDataFilter(col, "==", `val`)

        fun ne(col: String, `val`: Any) = ChartDataFilter(col, "!=", `val`)

        fun gt(col: String, `val`: Any) = ChartDataFilter(col, ">", `val`)

        fun lt(col: String, `val`: Any) = ChartDataFilter(col, "<", `val`)

        fun ge(col: String, `val`: Any) = ChartDataFilter(col, ">=", `val`)

        fun le(col: String, `val`: Any) = ChartDataFilter(col, "<=", `val`)

        fun like(col: String, `val`: String) = ChartDataFilter(col, "LIKE", `val`)

        fun isNull(col: String) = ChartDataFilter(col, "IS NULL", null)

        fun isNotNull(col: String) = ChartDataFilter(col, "IS NOT NULL", null)

        fun `in`(col: String, `val`: Iterable<*>) =
            ChartDataFilter(col, "IN", if (`val` is List<*>) `val` else `val`.toList())

        fun notIn(col: String, `val`: Iterable<*>) =
            ChartDataFilter(col, "NOT IN", if (`val` is List<*>) `val` else `val`.toList())

        // TODO find usages and ... ?
        fun between(col: String, type: Int, beginValInclusive: Any, endValInclusive: Any): List<ChartDataFilter> {
            var beginValInclusive = beginValInclusive
            var endValInclusive = endValInclusive
            val list = mutableListOf<ChartDataFilter>()
            if (type == 1) {
                beginValInclusive = "$beginValInclusive.000000"
                endValInclusive = "$endValInclusive.000000"
            }
            list.add(ChartDataFilter(col, ">=", beginValInclusive))
            list.add(ChartDataFilter(col, "<=", endValInclusive))
            return list
        }
    }

}