/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.sdk.coroutine.book

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

suspend fun main() = withContext(CoroutineName("Outer")) {
    printName()

    launch(CoroutineName("Inner")) {
        printName()
    }

    delay(10)
    printName()
}

suspend fun printName() {
    // coroutineContext is available in every suspending scope
    println(coroutineContext[CoroutineName]?.name)
}
