/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.ext

import top.viclau.magicbox.box.stats.dsl.GroupStep
import top.viclau.magicbox.box.stats.dsl.OrderStep
import top.viclau.magicbox.box.stats.dsl.PageStep
import top.viclau.magicbox.box.stats.dsl.model.PageReq

fun <DEST_TYPE : Any> GroupStep<DEST_TYPE>.page(page: Int, size: Int): PageStep<DEST_TYPE> = order {}.page(page, size)

fun <DEST_TYPE : Any> GroupStep<DEST_TYPE>.page(pageReq: PageReq): PageStep<DEST_TYPE> = order {}.page(pageReq)

fun <DEST_TYPE : Any> OrderStep<DEST_TYPE>.page(page: Int, size: Int): PageStep<DEST_TYPE> = page { page(page, size) }

fun <DEST_TYPE : Any> OrderStep<DEST_TYPE>.page(pageReq: PageReq): PageStep<DEST_TYPE> = page { page(pageReq) }
