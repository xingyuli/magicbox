/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.kvsj

import org.junit.Test

class UseJavaObjectTest {

    @Test
    fun test() {
        // Object -> Any?
        UseJavaObject.doSomething(Any())
    }

}