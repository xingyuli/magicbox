package top.viclau.magicbox.box.stats.ext

import java.time.LocalDateTime

class TimeRange(val begin: LocalDateTime, val end: LocalDateTime, val includeEnd: Boolean = true)

fun LocalDateTime.range(end: LocalDateTime, includeEnd: Boolean = true): TimeRange =
    TimeRange(this, end, includeEnd)
