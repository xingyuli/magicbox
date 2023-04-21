/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.ext

import top.viclau.magicbox.box.stats.dsl.SelectStep
import top.viclau.magicbox.box.stats.model.DialectScope
import top.viclau.magicbox.box.stats.model.QueryDialect

fun <DEST_TYPE : Any, S : DialectScope, DIALECT : QueryDialect<*, S, *>> SelectStep<DEST_TYPE>.WhereableScope<S, DIALECT>.where(
    base: S.() -> Unit,
    builder: S.() -> Unit
) {
    where {
        base()
        builder()
    }
}
