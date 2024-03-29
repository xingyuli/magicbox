/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.ext

import top.viclau.magicbox.box.stats.dsl.PageStep
import top.viclau.magicbox.box.stats.dsl.SummaryStep

fun <DEST_TYPE : Any> PageStep<DEST_TYPE>.summaryWith(builder: (DEST_TYPE) -> Unit): SummaryStep<DEST_TYPE> =
    summary { with(builder) }
