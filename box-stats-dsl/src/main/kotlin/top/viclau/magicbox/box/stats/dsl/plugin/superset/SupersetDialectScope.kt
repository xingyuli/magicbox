package top.viclau.magicbox.box.stats.dsl.plugin.superset

import top.viclau.magicbox.box.stats.dsl.Builder
import top.viclau.magicbox.box.stats.ext.TimeRange
import top.viclau.magicbox.box.stats.ext.isNotEmpty
import top.viclau.magicbox.box.stats.ext.toSeparatedSet
import top.viclau.magicbox.box.stats.model.DialectScope

class SupersetDialectScope : DialectScope {

    internal var timeRange: TimeRangeOnColumn? = null

    // currently SqlFilter has no name, so place into the Map here
    internal val conditionalBuilders = mutableMapOf<String, MutableList<ConditionalFilterBuilder>>()

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

    infix fun String.between(timeRange: TimeRange) {
        this@SupersetDialectScope.timeRange =
            TimeRangeOnColumn(this, timeRange.begin, timeRange.end, timeRange.includeEnd)
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

