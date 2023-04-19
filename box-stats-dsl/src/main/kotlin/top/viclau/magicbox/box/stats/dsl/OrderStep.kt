/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl

import top.viclau.magicbox.box.stats.model.Direction
import top.viclau.magicbox.box.stats.model.Order
import kotlin.reflect.KProperty1

class OrderStep<DEST_TYPE : Any>(private val dsl: DslBuilder<DEST_TYPE>) : Builder<Order?> {

    private lateinit var by: Order.By
    private lateinit var dir: Direction

    private fun byDir(by: Order.By, dir: Direction) {
        this.by = by
        this.dir = dir
    }

    fun page(builder: PageStep<DEST_TYPE>.Scope.() -> Unit): PageStep<DEST_TYPE> =
        dsl.page.apply { Scope().builder() }

    override fun invoke(): Order? = if (::by.isInitialized) Order(by, dir) else null

    inner class Scope internal constructor() {

        fun by(by: KProperty1<DEST_TYPE, Comparable<*>?>, dir: Direction) {
            byDir(Order.ByProp(by), dir)
        }

        fun by(by: String, dir: Direction) {
            byDir(Order.ByPropName(by), dir)
        }

    }

}
