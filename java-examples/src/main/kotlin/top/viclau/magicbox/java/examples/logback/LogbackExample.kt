/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.logback

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory


private fun `chapter1 print logger status`() {
    // Logback can report information about its internal state using a
    // built-in status system. Important events occurring during logback's
    // lifetime can be accessed through a component called StatusManager.
    val lc = LoggerFactory.getILoggerFactory() as LoggerContext
    StatusPrinter.print(lc)

    // Note that in the above example we have instructed logback to print
    // its internal state by invoking the StatusPrinter.print() method.
    // Logback's internal status information can be very useful in
    // diagnosing logback-related problems.

    // Logback explains that having failed to find the logback-test.xml and
    // logback.xml configuration files (discussed later), it configured
    // itself using its default policy, which is a basic ConsoleAppender.
}

private fun `chapter2 level inheritance`() {
    // If a given logger is not assigned a level, then it inherits one from
    // its closest ancestor with an assigned level.

    // To ensure that all loggers can eventually inherit a level, the root
    // logger always has an assigned level. By default, this level is
    // DEBUG.
    val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

    // prints true
    println(rootLogger.isDebugEnabled)
}

private fun `chapter2 retrieve logger with the same name`() {
    val l1 = LoggerFactory.getLogger("viclau")
    val l2 = LoggerFactory.getLogger("viclau")

    // prints true
    println(l1 == l2)
}

fun main() {
    `chapter1 print logger status`()

    `chapter2 level inheritance`()

    `chapter2 retrieve logger with the same name`()
}
