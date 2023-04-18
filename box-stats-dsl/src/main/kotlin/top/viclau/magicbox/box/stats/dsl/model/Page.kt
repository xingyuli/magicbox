/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.model

data class PageReq(
    /**
     * 0 based.
     */
    val page: Int,
    val size: Int = 10
)

class PageResult<DEST_TYPE>(val page: Int, val size: Int)
