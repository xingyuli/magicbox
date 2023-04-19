/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl

import top.viclau.magicbox.box.stats.model.PageReq

class PageStep<DEST_TYPE : Any>(private val dsl: DslBuilder<DEST_TYPE>) : Builder<PageReq> {

    private lateinit var pageReq: PageReq

    fun summary(builder: SummaryStep<DEST_TYPE>.Scope.() -> Unit): SummaryStep<DEST_TYPE> =
        dsl.summary.apply { Scope().builder() }

    override fun invoke(): PageReq = pageReq

    inner class Scope internal constructor() {

        fun page(page: Int, size: Int) {
            page(PageReq(page, size))
        }

        fun page(pageReq: PageReq) {
            this@PageStep.pageReq = pageReq
        }

    }

}
