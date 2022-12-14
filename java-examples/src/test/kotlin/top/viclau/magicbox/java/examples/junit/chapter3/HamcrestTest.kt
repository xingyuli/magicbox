package top.viclau.magicbox.java.examples.junit.chapter3

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


@Disabled("Ignore as this is only a demo for illustrating the Hamcrest matcher")
class HamcrestTest {

    private lateinit var values: MutableList<String>

    @BeforeEach
    fun setUpList() {
        values = ArrayList()
        values.add("one")
        values.add("two")
        values.add("three")
    }

    @Test
    fun testWithoutHamcrest() {
        assertTrue(values.contains("one")
                || values.contains("two")
                || values.contains("three"))
    }

    @Test
    fun testWithHamcrest() {
        assertThat<List<String>>(
            values, hasItem(
                anyOf(
                    equalTo("one"),
                    equalTo("two"),
                    equalTo("three")
                )
            )
        )
    }

}