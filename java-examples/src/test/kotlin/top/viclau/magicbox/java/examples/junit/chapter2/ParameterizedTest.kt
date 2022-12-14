package top.viclau.magicbox.java.examples.junit.chapter2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

class ParameterizedTest {

    @ParameterizedTest
    @MethodSource("data")
    fun sum(expected: Double, valueOne: Double, valueTwo: Double) {
        val calc = Calculator()
        assertEquals(expected, calc.add(valueOne, valueTwo))
    }

    companion object {

        @JvmStatic
        fun data(): List<Array<Int>> {
            return listOf(arrayOf(2, 1, 1), arrayOf(3, 2, 1), arrayOf(4, 3, 1))
        }

    }

}