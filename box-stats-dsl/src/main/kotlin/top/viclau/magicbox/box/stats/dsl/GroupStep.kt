/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl

import top.viclau.magicbox.box.stats.dsl.model.Group
import top.viclau.magicbox.box.stats.dsl.model.GroupBy
import top.viclau.magicbox.box.stats.dsl.model.GroupByExtract
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

class GroupStep<DEST_TYPE : Any>(private val dsl: DslBuilder<DEST_TYPE>) : Builder<Group<DEST_TYPE>> {

    private val bys = mutableListOf<GroupByBuilder<*, DEST_TYPE>>()

    internal fun add(by: GroupByBuilder<*, DEST_TYPE>) {
        bys.add(by)
    }

    fun order(builder: OrderStep<DEST_TYPE>.Scope.() -> Unit): OrderStep<DEST_TYPE> =
        dsl.order.apply { Scope().builder() }

    override fun invoke(): Group<DEST_TYPE> = Group(bys.map { it() })

    inner class Scope internal constructor() {
        fun <R : Any> by(
            by: KProperty1<*, *>,
            dimensionRecordMapper: (List<String>) -> List<R>,
            dimensionValue: (R) -> Any?,
            builder: GroupByBuilder<R, DEST_TYPE>.Scope.() -> Unit
        ) {
            with(GroupByBuilder(this@GroupStep, by, dimensionRecordMapper, dimensionValue)) {
                Scope().builder()
                done()
            }
        }
    }

}

class GroupByBuilder<R : Any, DEST_TYPE : Any>(
    private val group: GroupStep<DEST_TYPE>,
    private val by: KProperty1<*, *>,
    private val dimensionRecordMapper: (List<String>) -> List<R>,
    private val dimensionValue: (R) -> Any?
) : Builder<GroupBy<R, DEST_TYPE>> {

    private val extracts = mutableListOf<GroupByExtractBuilder<R, *, DEST_TYPE>>()

    internal fun add(extract: GroupByExtractBuilder<R, *, DEST_TYPE>) {
        extracts.add(extract)
    }

    internal fun done() {
        group.add(this)
    }

    override fun invoke(): GroupBy<R, DEST_TYPE> =
        GroupBy(by, dimensionRecordMapper, dimensionValue, extracts.map { it() })

    inner class Scope internal constructor() {

        infix fun <R_PROP> KProperty1<R, R_PROP?>.`as`(alias: KMutableProperty1<DEST_TYPE, R_PROP?>) {
            GroupByExtractBuilder(this@GroupByBuilder, this).Scope().`as`(alias)
        }

        internal fun extractString(): GroupByExtractStringBuilder<DEST_TYPE>.Scope {
            @Suppress("UNCHECKED_CAST")
            val groupBy = this@GroupByBuilder as GroupByBuilder<String, DEST_TYPE>
            return GroupByExtractStringBuilder(groupBy).Scope()
        }

    }

}

// R: type of the fetched data
// R_PROP: type of the fetched data's property
open class GroupByExtractBuilder<R : Any, R_PROP, DEST_TYPE : Any> internal constructor(
    private val groupBy: GroupByBuilder<R, DEST_TYPE>,
    private val src: (R) -> R_PROP?
) : Builder<GroupByExtract<R, R_PROP, DEST_TYPE>> {

    private lateinit var alias: KMutableProperty1<DEST_TYPE, R_PROP?>

    private fun `as`(alias: KMutableProperty1<DEST_TYPE, R_PROP?>) {
        this.alias = alias
        groupBy.add(this)
    }

    override fun invoke(): GroupByExtract<R, R_PROP, DEST_TYPE> = GroupByExtract(src, alias)

    open inner class Scope internal constructor() {

        fun `as`(alias: KMutableProperty1<DEST_TYPE, R_PROP?>) {
            this@GroupByExtractBuilder.`as`(alias)
        }

    }

}

// special case
class GroupByExtractStringBuilder<DEST_TYPE : Any>(groupBy: GroupByBuilder<String, DEST_TYPE>) :
    GroupByExtractBuilder<String, String, DEST_TYPE>(groupBy, { it }) {
    inner class Scope internal constructor() : GroupByExtractBuilder<String, String, DEST_TYPE>.Scope()
}
