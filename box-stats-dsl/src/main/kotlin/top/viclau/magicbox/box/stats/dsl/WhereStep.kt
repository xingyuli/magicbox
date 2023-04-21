/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl

import top.viclau.magicbox.box.stats.model.DialectScope
import top.viclau.magicbox.box.stats.model.QueryDialect
import top.viclau.magicbox.box.stats.model.Where

class WhereStep<DEST_TYPE : Any, S : DialectScope>(
    private val dsl: DslBuilder<DEST_TYPE>,
    private val dialect: QueryDialect<*, S, *>,
    private val scope: S
) : Selectable<DEST_TYPE> by dsl, Builder<Where> {

    fun group(builder: GroupStep<DEST_TYPE>.Scope.() -> Unit): GroupStep<DEST_TYPE> =
        dsl.group.apply { Scope().builder() }

    override fun invoke(): Where = dialect.toModel(scope)

}
