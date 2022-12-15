/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.regex

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.regex.Pattern

class RegexTest {

    @Test
    fun backReference1() {
        assertTrue(Pattern.matches("(\\d\\d)\\1\\1", "121212"))
    }

    @Test
    fun backReference2() {
        assertTrue(Pattern.matches("(\\d{4})\\w{2}\\1", "1111aa1111"))
    }

}