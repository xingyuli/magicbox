package top.viclau.magicbox.java.examples.joda

import org.joda.time.DateTime
import org.joda.time.MutableDateTime
import java.util.*

fun now(): DateTime = DateTime.now()

fun getRemindDate(date: Date, after: Int): DateTime = with(MutableDateTime(date)) {
    addDays(after)
    setTime(10, 0, 0, 0)
    toDateTime()
}

fun firstDayOfCurrentMonth(): DateTime = with(MutableDateTime.now()) {
    dayOfMonth = 1
    millisOfDay = 0
    toDateTime()
}

fun lastDayOfCurrentMonth(): DateTime = with(MutableDateTime.now()) {
    dayOfMonth = dayOfMonth().maximumValue
    millisOfDay = millisOfDay().maximumValue
    toDateTime()
}

fun rightBeforeToday(): DateTime = rightBefore(today()).toDateTime()

fun rightAfterToday(): DateTime = rightAfter(today()).toDateTime()

fun rightBeforeTomorrow(): DateTime = rightBefore(tomorrow()).toDateTime()

fun rightAfterTomorrow(): DateTime = rightAfter(tomorrow()).toDateTime()

private fun today(): MutableDateTime = MutableDateTime.now()

private fun tomorrow(): MutableDateTime = MutableDateTime.now().apply {
    addDays(1)
}

private fun rightBefore(mdt: MutableDateTime): MutableDateTime = mdt.apply {
    addDays(-1)
    millisOfDay = millisOfDay().maximumValue
}

private fun rightAfter(mdt: MutableDateTime): MutableDateTime = mdt.apply {
    addDays(1)
    millisOfDay = 0
}