/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl

import top.viclau.magicbox.box.stats.dsl.ext.isNotEmpty
import top.viclau.magicbox.box.stats.dsl.ext.toSeparatedSet
import top.viclau.magicbox.box.stats.dsl.model.SqlFilter
import top.viclau.magicbox.box.stats.dsl.model.TimeRangeOnColumn
import top.viclau.magicbox.box.stats.dsl.model.Where
import java.time.LocalDateTime

class WhereStep<DEST_TYPE : Any>(private val dsl: DslBuilder<DEST_TYPE>) : Selectable<DEST_TYPE> by dsl,
    Builder<Where> {

    // TODO possible to specify the timeRange
    private var timeRange: TimeRangeOnColumn? = null

    // TODO currently SqlFilter has no name, so place into the Map here
    private val conditionalBuilders = mutableMapOf<String, MutableList<ConditionalFilterBuilder>>()

    private fun add(
        name: String,
        builder: () -> SqlFilter,
        value: () -> Boolean
    ): ConditionalFilterBuilder.Scope {
        val conditionalFilterBuilder = ConditionalFilterBuilder(builder, value)
        conditionalBuilders.getOrPut(name) { mutableListOf() }.add(conditionalFilterBuilder)
        return conditionalFilterBuilder.Scope()
    }

    private fun remove(filterName: String) {
        conditionalBuilders.remove(filterName)
    }

    fun group(builder: GroupStep<DEST_TYPE>.Scope.() -> Unit): GroupStep<DEST_TYPE> =
        dsl.group.apply { Scope().builder() }

    override fun invoke(): Where = Where(timeRange, conditionalBuilders.values.flatMap { it.mapNotNull { it() } })

    class TimeRange(val begin: LocalDateTime, val end: LocalDateTime, val includeEnd: Boolean = true)

    inner class Scope internal constructor() {

        infix fun String.between(timeRange: TimeRange) {
            this@WhereStep.timeRange = TimeRangeOnColumn(this, timeRange.begin, timeRange.end, timeRange.includeEnd)
        }

        infix fun String.filter(builder: SqlFilter.Companion.(String) -> SqlFilter): ConditionalFilterBuilder.Scope =
            add(this, { SqlFilter.builder(this) }, { true })

        fun include(vararg filterName: String) {
            exclude(*(conditionalBuilders.keys - filterName.toSet()).toTypedArray())
        }

        fun exclude(vararg filterName: String) {
            filterName.forEach { remove(it) }
        }

        /* ***** shortcuts ***** */

        infix fun String.`in`(values: Iterable<Any>) = `in` { values }

        infix fun String.`in`(values: () -> Iterable<Any>) = filter { `in`(it, values()) }

        infix fun String.optIn(commaSeparated: String?) {
            optIn { commaSeparated }
        }

        infix fun String.optIn(commaSeparated: () -> String?) {
            filter { `in`(it, commaSeparated().toSeparatedSet()) } `if` { commaSeparated().isNotEmpty() }
        }

        infix fun String.eq(value: Any) = eq { value }

        infix fun String.eq(value: () -> Any) = filter { eq(it, value()) }

        infix fun String.ne(value: Any) = ne { value }

        infix fun String.ne(value: () -> Any) = filter { notEq(it, value()) }

        infix fun String.like(value: String?) {
            like { value }
        }

        infix fun String.like(value: () -> String?) {
            filter { like(it, value()!!) } `if` { value().isNotEmpty() }
        }

        fun String.notNull() {
            this filter { SqlFilter.notNull(it) }
        }

        fun String.notBlank() {
            this filter { SqlFilter.notBlank(it) }
        }

        fun String.isTrue() {
            this filter { SqlFilter.isTrue(it) }
        }

    }

}

class ConditionalFilterBuilder(private val builder: () -> SqlFilter, private var value: () -> Boolean) :
    Builder<SqlFilter?> {

    override fun invoke(): SqlFilter? = if (value()) builder() else null

    inner class Scope internal constructor() {

        infix fun `if`(value: Boolean) {
            `if` { value }
        }

        infix fun `if`(value: () -> Boolean) {
            this@ConditionalFilterBuilder.value = value
        }

    }

}

fun LocalDateTime.range(end: LocalDateTime, includeEnd: Boolean = true): WhereStep.TimeRange =
    WhereStep.TimeRange(this, end, includeEnd)
