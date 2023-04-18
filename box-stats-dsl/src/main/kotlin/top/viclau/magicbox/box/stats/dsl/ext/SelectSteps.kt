/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.ext

import top.viclau.magicbox.box.stats.dsl.SelectStep
import top.viclau.magicbox.box.stats.dsl.WhereStep

fun <DEST_TYPE : Any> SelectStep<DEST_TYPE>.WhereableScope.where(
    base: WhereStep<DEST_TYPE>.Scope.() -> Unit,
    builder: WhereStep<DEST_TYPE>.Scope.() -> Unit
) {
    where {
        base()
        builder()
    }
}
