/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.text.SimpleDateFormat
import java.util.*


class DateSerializer : StdSerializer<Date>(Date::class.java) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun serialize(value: Date, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeString(dateFormat.format(value))
    }

}