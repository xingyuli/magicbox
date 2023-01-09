/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.kvsj

class CompanionSingleton private constructor() {

    companion object {

        val instance: CompanionSingleton by lazy {
            CompanionSingleton()
        }

    }

}

class StandardSingleton private constructor() {

    companion object {

        private val instance_: StandardSingleton by lazy {
            StandardSingleton()
        }

        @JvmStatic
        fun getInstance() = instance_

    }

}

object ShorthandSingleton {
    init {
        println("do some preparation here")
    }
}