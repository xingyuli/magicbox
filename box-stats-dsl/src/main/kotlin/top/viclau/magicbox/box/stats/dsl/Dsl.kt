/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl

import top.viclau.magicbox.box.stats.model.*
import kotlin.reflect.KClass

typealias Builder<T> = () -> T

interface Selectable<DEST_TYPE : Any> {
    fun select(builder: SelectStep<DEST_TYPE>.Scope.() -> Unit): SelectStep<DEST_TYPE>
    fun selectWhere(builder: SelectStep<DEST_TYPE>.WhereableScope.() -> Unit): WhereStep<DEST_TYPE>
}

interface Executable<DEST_TYPE> {
    fun execute(): PageResult<DEST_TYPE>
}

class SelectWhereStepTuple<DEST_TYPE : Any>(val select: SelectStep<DEST_TYPE>, val where: WhereStep<DEST_TYPE>)

class DslBuilder<DEST_TYPE : Any>(private val destTypeClass: KClass<DEST_TYPE>, val config: Query.Config) : Selectable<DEST_TYPE>,
    Builder<Query<DEST_TYPE>> {

    private val selectWheres = mutableListOf<SelectWhereStepTuple<DEST_TYPE>>()

    internal val group by lazy { GroupStep(this) }
    internal val order by lazy { OrderStep(this) }
    internal val page by lazy { PageStep(this) }
    internal val summary by lazy { SummaryStep(this) }

    internal fun addSelectWhere(selectWhere: SelectWhereStepTuple<DEST_TYPE>) {
        selectWheres.add(selectWhere)
    }

    override fun select(builder: SelectStep<DEST_TYPE>.Scope.() -> Unit): SelectStep<DEST_TYPE> =
        SelectStep(this).apply { Scope().builder() }

    override fun selectWhere(builder: SelectStep<DEST_TYPE>.WhereableScope.() -> Unit): WhereStep<DEST_TYPE> =
        SelectStep(this).apply { WhereableScope().builder() }.where

    override fun invoke(): Query<DEST_TYPE> = Query(
        destTypeClass,
        config,
        selectWheres.map { SelectWhere(it.select(), it.where()) },
        group(),
        order(),
        page(),
        summary()
    )

    // TODO later - override toString: print as structural string

}

fun <DEST_TYPE : Any> statsDsl(kClass: KClass<DEST_TYPE>, config: Query.Config): DslBuilder<DEST_TYPE> {
    return DslBuilder(kClass, config)
}
