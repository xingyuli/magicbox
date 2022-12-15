/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.logback

import org.slf4j.LoggerFactory

class Slf4jExample {

    private val logger = LoggerFactory.getLogger(Slf4jExample::class.java)

    fun helloWorld() {
        logger.info("Hello World")
    }

    fun typicalUsagePattern() {
        val t = 0
        val oldT = -7
        logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT)
    }

    fun misc() {
        logger.debug("Set {1,2} differs from {}", 3)
        logger.debug("Set {1,2} differs from {{}}", 3)
        logger.debug("Set \\{} differs from {}", 3)
        logger.debug("Something went wrong when scenario is {}", "TestingScenario", RuntimeException("test"))
    }

}

fun main() {
    Slf4jExample().apply {
        helloWorld()
        typicalUsagePattern()
        misc()
    }
}