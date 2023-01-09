/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.jackson

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class DateSerializerTest {

    private lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        val testModule = SimpleModule("MyModule", Version(1, 0, 0, null, "my-app", "app-layer"))
        testModule.addSerializer(DateSerializer())

        mapper = ObjectMapper().registerModule(testModule)
    }

    @Test
    fun testSerializeSingleDate() {
        assertEquals("\"2015-11-25 00:00:00\"", mapper.writeValueAsString(getTestDate()))
    }

    @Test
    fun testSerializeMapWithDate() {
        val expected = """{"now":"2015-11-25 00:00:00"}"""
        assertEquals(expected, mapper.writeValueAsString(mapOf("now" to getTestDate())))
    }

    @Test
    fun testSerializeBeanWithDate() {
        val expected = """{"now":"2015-11-25 00:00:00"}"""
        assertEquals(expected, mapper.writeValueAsString(
            MyBean(getTestDate())
        ))
    }

    class MyBean(val now: Date)

    companion object {

        private const val DATE_STR = "2015-11-25"

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        fun getTestDate(): Date = dateFormat.parse(DATE_STR)

    }

}