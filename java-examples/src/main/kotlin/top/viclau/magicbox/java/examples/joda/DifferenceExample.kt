/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.joda

import org.joda.time.Days
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.ReadableInstant
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.PeriodFormat

private val formatter = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss")

private fun differenceOfDays(start: ReadableInstant, end: ReadableInstant): Days =
    Days.daysBetween(start, end)

private fun differenceOfPeriodType(start: ReadableInstant, end: ReadableInstant, type: PeriodType): Period =
    Period(start.millis, end.millis, type)

fun main() {
    // 3
    println(
        differenceOfDays(
            formatter.parseDateTime("20150109 10:00:00"),
            formatter.parseDateTime("20150113 09:00:00")
        ).days
    )

    // 4
    println(
        differenceOfDays(
            formatter.parseDateTime("20150109 10:00:00"),
            formatter.parseDateTime("20150113 11:00:00")
        ).days
    )

    // 15 years, 2 months, 9 days, 1 hour, 11 minutes and 11 seconds
    val p = differenceOfPeriodType(
        formatter.parseDateTime("20000101 10:00:00"),
        formatter.parseDateTime("20150310 11:11:11"),
        PeriodType.yearMonthDayTime()
    )
    println(PeriodFormat.wordBased().print(p))
}
