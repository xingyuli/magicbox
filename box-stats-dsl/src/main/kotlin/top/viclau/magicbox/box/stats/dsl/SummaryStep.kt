/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl

import top.viclau.magicbox.box.stats.dsl.model.PageResult
import top.viclau.magicbox.box.stats.dsl.model.Summary

class SummaryStep<DEST_TYPE : Any>(private val dsl: DslBuilder<DEST_TYPE>) : Executable<DEST_TYPE>,
    Builder<Summary<DEST_TYPE>?> {

    private lateinit var with: (DEST_TYPE) -> Unit

    override fun execute(): PageResult<DEST_TYPE> = dsl().execute()

    override fun invoke(): Summary<DEST_TYPE>? = if (::with.isInitialized) { Summary(with) } else null

    inner class Scope internal constructor() {

        fun with(builder: (DEST_TYPE) -> Unit) {
            this@SummaryStep.with = builder
        }

    }

}