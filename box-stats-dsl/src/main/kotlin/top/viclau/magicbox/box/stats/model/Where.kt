/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model

// TODO viclau solve engine specific dependency
import top.viclau.magicbox.box.stats.integration.superset.chart.data.ChartDataFilter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Where(val timeRange: TimeRangeOnColumn?, filters: List<SqlFilter>) {

    // TODO generalize the Filter concept ?
    private var _filters = filters

    fun add(filter: SqlFilter) {
        _filters += filter
    }

    val filters: List<SqlFilter> get() = _filters

}

// TODO this is superset specific
data class TimeRangeOnColumn(
    val column: String,
    val begin: LocalDateTime,
    val end: LocalDateTime,
    val includeEnd: Boolean
) : () -> String {

    override fun invoke(): String = "${begin.format(FORMATTER)} : ${end.format(FORMATTER)}"

    companion object {

        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    }

}

interface SqlFilter : () -> String {

    companion object {

        /**
         * Produces sql: `column is not null` .
         *
         * @param column
         * @return
         */
        fun notNull(column: String): SqlFilter = NotNull(column)

        /**
         * Produces sql: `column != ''`, this implies that the specified column must have varchar type.
         *
         * @param column
         * @return
         */
        fun notEmpty(column: String): SqlFilter = NotEmpty(column)

        /**
         * Produces sql: `trim(column) != ''` . This implies that the specified column must have varchar type.
         *
         * @param column
         * @return
         */
        fun notBlank(column: String): SqlFilter = NotBlank(column)

        /**
         * For numeric values, produces sql: `column = value` .<br></br>
         * <br></br>
         * For other values, produces sql: `column = 'value.toString()'` .
         *
         * @param column
         * @param value
         * @return
         */
        fun eq(column: String, value: Any): SqlFilter = Eq(column, value)

        /**
         * For numeric values, produces sql: `column != value` .<br></br>
         * <br></br>
         * For other values, produces sql: `column != 'value.toString()'` .
         *
         * @param column
         * @param value
         * @return
         */
        fun notEq(column: String, value: Any): SqlFilter = NotEq(column, value)

        /**
         * Produces sql: `column LIKE 'value'` .
         *
         * @param column
         * @param value
         * @return
         */
        fun like(column: String, value: String): SqlFilter = Like(column, value)

        /**
         * For numeric values, produces sql: `column BETWEEN lowerValue AND upperValue` .<br></br>
         * <br></br>
         * For other values, produces sql: `column BETWEEN 'lowerValue.toString()' AND 'upperValue.toString()'` .
         *
         * @param column
         * @param lowerValue
         * @param upperValue
         * @return
         */
        fun between(column: String, lowerValue: Any, upperValue: Any): SqlFilter =
            Between(column, lowerValue, upperValue)

        /**
         * For numeric values, produces sql: `column IN (value1, value2, ..., valueN)` .<br></br>
         * <br></br>
         * For other values, produces sql: `column IN ('value1.toString()', 'value2.toString()', ..., 'valueN.toString()')` .
         *
         * @param column
         * @param values
         * @return
         */
        fun `in`(column: String, values: Iterable<*>): SqlFilter = In(column, values.toList())

        /**
         * For numeric values, produces sql: `column NOT IN (value1, value2, ..., valueN)` .<br></br>
         * <br></br>
         * For other values, produces sql: `column NOT IN ('value1.toString()', 'value2.toString()', ..., 'valueN.toString()')` .
         *
         * @param column
         * @param values
         * @return
         */
        fun notIn(column: String, values: Iterable<*>): SqlFilter = NotIn(column, values.toList())

        /**
         * Produces sql: `column = True` .
         *
         * @param column
         * @return
         */
        fun isTrue(column: String): SqlFilter = IsTrue(column)

        /**
         * Produces sql as it is given.
         *
         * @param sql
         * @return
         */
        fun string(sql: String): SqlFilter = object : SqlFilter {
            override fun invoke(): String = sql
        }

    }

}

sealed class SpecificColumnSqlFilter(val column: String) : SqlFilter {

    abstract val expression: String

    override fun invoke(): String = "$column $expression"

    protected fun wrapInSql(value: Any?): String = when (value) {
        is Number -> value.toString()
        null -> "null"
        else -> "'$value'"
    }

}

// TODO NOTE: sql operators that supported by Superset Chart Data API should implement this interface. implement as extension function
internal interface ChartDataFilterShim {
    fun toChartDataFilter(): ChartDataFilter
}

class NotNull internal constructor(column: String) : SpecificColumnSqlFilter(column), ChartDataFilterShim {

    override val expression: String = "is not null"

    override fun toChartDataFilter() = ChartDataFilter.isNotNull(column)

}

class NotEmpty internal constructor(column: String) : SpecificColumnSqlFilter(column), ChartDataFilterShim {

    override val expression: String = "!= ''"

    override fun toChartDataFilter() = ChartDataFilter.ne(column, "")

}

class NotBlank internal constructor(column: String) : SpecificColumnSqlFilter(column) {

    override val expression: String = "trim($column) != ''"

    override fun invoke(): String = expression

}

class Eq internal constructor(column: String, private val value: Any) : SpecificColumnSqlFilter(column),
    ChartDataFilterShim {

    override val expression: String = "= " + wrapInSql(value)

    override fun toChartDataFilter() = ChartDataFilter.eq(column, value)

}

class NotEq internal constructor(column: String, private val value: Any) : SpecificColumnSqlFilter(column),
    ChartDataFilterShim {

    override val expression: String = "!= " + wrapInSql(value)

    override fun toChartDataFilter() = ChartDataFilter.ne(column, value)

}

class Like internal constructor(column: String, private val value: String) : SpecificColumnSqlFilter(column),
    ChartDataFilterShim {

    override val expression: String = "LIKE '$value'"

    override fun toChartDataFilter() = ChartDataFilter.like(column, value)

}

class Between internal constructor(column: String, lowerValue: Any, upperValue: Any) :
    SpecificColumnSqlFilter(column) {

    private val lowerSql: String = wrapInSql(lowerValue)
    private val upperSql: String = wrapInSql(upperValue)

    override val expression: String = "BETWEEN $lowerSql AND $upperSql"

}

class In internal constructor(column: String, private val values: Collection<*>) : SpecificColumnSqlFilter(column),
    ChartDataFilterShim {

    override val expression: String = "IN (${values.joinToString(separator = ",", transform = ::wrapInSql)})"

    override fun toChartDataFilter() = ChartDataFilter.`in`(column, values)

}

class NotIn internal constructor(column: String, private val values: Collection<*>) : SpecificColumnSqlFilter(column),
    ChartDataFilterShim {

    override val expression: String = "NOT IN (${values.joinToString(separator = ",", transform = ::wrapInSql)})"

    override fun toChartDataFilter() = ChartDataFilter.notIn(column, values)

}

class IsTrue internal constructor(column: String) : SpecificColumnSqlFilter(column) {
    override val expression: String = "= True"
}
