/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SnakeCaseTest {

    private lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mapper = ObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
        }
    }

    @Test
    fun testDeserialization() {
        val project = mapper.readValue(
            """{
"first_name": "Vic",
"last_name": "Lau",
"gender": "Male"
}""", Project::class.java
        )
        assertEquals("Vic", project.firstName)
        assertEquals("Lau", project.lastName)
        assertEquals("Male", project.gender)
    }

    @Test
    fun testSerialization() {
        val project = Project().apply {
            firstName = "Vic"
            lastName = "Lau"
            gender = "Male"
        }
        val projectJson = mapper.writeValueAsString(project)
        assertEquals("""{"first_name":"Vic","last_name":"Lau","gender":"Male"}""", projectJson)
    }

    class Project {
        var firstName: String? = null
        var lastName: String? = null
        var gender: String? = null
    }

}