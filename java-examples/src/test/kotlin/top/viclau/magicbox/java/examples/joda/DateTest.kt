/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.joda

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.format.DateTimeFormat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class DateTest {

    companion object {

        private val dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss")

        @JvmStatic
        @BeforeAll
        fun beforeClass() {
            DateTimeUtils.setCurrentMillisFixed(DateTime.parse("20150120 11:20:30", dateTimeFormatter).millis)
        }

    }

    @Test
    fun testNow() {
        verifyOutput("20150120 11:20:30", now())
    }

    @Test
    fun testGetRemindDate() {
        verifyOutput("20150123 10:00:00", getRemindDate(now().toDate(), 3))
    }

    @Test
    fun testFirstDayOfCurrentMonth() {
        verifyOutput("20150101 00:00:00", firstDayOfCurrentMonth())
    }

    @Test
    fun testLastDayOfCurrentMonth() {
        verifyOutput("20150131 23:59:59", lastDayOfCurrentMonth())
    }

    @Test
    fun testRightBeforeToday() {
        verifyOutput("20150119 23:59:59", rightBeforeToday())
    }

    @Test
    fun testRightAfterToday() {
        verifyOutput("20150121 00:00:00", rightAfterToday())
    }

    @Test
    fun testRightBeforeTomorrow() {
        verifyOutput("20150120 23:59:59", rightBeforeTomorrow())
    }

    @Test
    fun testRightAfterTomorrow() {
        verifyOutput("20150122 00:00:00", rightAfterTomorrow())
    }

    private fun verifyOutput(expected: String, actual: DateTime) {
        assertThat(actual.toString(dateTimeFormatter), CoreMatchers.`is`(expected))
    }

}