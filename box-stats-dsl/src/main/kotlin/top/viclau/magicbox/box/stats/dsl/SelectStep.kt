/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl

import top.viclau.magicbox.box.stats.model.*
import java.math.BigDecimal
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

class SelectStep<DEST_TYPE : Any>(private val dsl: DslBuilder<DEST_TYPE>) : Builder<Select<DEST_TYPE>> {

    private lateinit var selectWhere: SelectWhereStepTuple<DEST_TYPE>

    internal val where: WhereStep<DEST_TYPE, *> get() = selectWhere.where

    private val aliasedPropertyBuilders = mutableListOf<AliasedPropertyBuilder<*, DEST_TYPE>>()
    private val computedPropertyBuilders = mutableListOf<ComputedPropertyBuilder<*, *, *, DEST_TYPE>>()

    internal fun add(builder: AliasedPropertyBuilder<*, DEST_TYPE>) {
        aliasedPropertyBuilders.add(builder)
    }

    internal fun add(builder: ComputedPropertyBuilder<*, *, *, DEST_TYPE>) {
        computedPropertyBuilders.add(builder)
    }

    fun <S : DialectScope, DIALECT : QueryDialect<*, S, *>> where(
        dialect: DIALECT,
        builder: S.() -> Unit
    ): WhereStep<DEST_TYPE, *> {
        if (this::selectWhere.isInitialized) {
            throw IllegalStateException("`where` should be called exactly once")
        }

        val scope = dialect()
        selectWhere = SelectWhereStepTuple(this, WhereStep(dsl, dialect, scope).apply { scope.builder() })
        dsl.addSelectWhere(selectWhere)

        return selectWhere.where
    }

    override fun invoke(): Select<DEST_TYPE> {
        val model = Select<DEST_TYPE>()
        aliasedPropertyBuilders.forEach { model.add(it()) }
        computedPropertyBuilders.forEach { model.add(it()) }
        return model
    }

    open inner class Scope internal constructor() {

        infix fun <SRC_PROP> KProperty1<*, SRC_PROP?>.`as`(alias: KMutableProperty1<DEST_TYPE, SRC_PROP?>) {
            AliasedPropertyBuilder(this@SelectStep, Select.PropType.NORMAL, this).`as`(alias)
        }

        operator fun <DIVIDEND : Number, DIVISOR : Number> KProperty1<*, DIVIDEND?>.div(srcY: KProperty1<*, DIVISOR?>): PropertyBuilder<BigDecimal, DEST_TYPE> =
            rate(this, srcY)

        private fun <DIVIDEND : Number, DIVISOR : Number> rate(
            srcX: KProperty1<*, DIVIDEND?>,
            srcY: KProperty1<*, DIVISOR?>
        ): PropertyBuilder<BigDecimal, DEST_TYPE> {
            val rateFn = dsl.config.rateFn
            return ComputedPropertyBuilder(this@SelectStep, Select.PropType.RATE, srcX, srcY, rateFn, "/")
        }

    }

    inner class WhereableScope<S : DialectScope, DIALECT : QueryDialect<*, S, *>> internal constructor(private val dialect: DIALECT) :
        Scope() {
        fun where(builder: S.() -> Unit) {
            this@SelectStep.where(dialect, builder)
        }
    }

}

sealed class PropertyBuilder<SRC_PROP, DEST_TYPE : Any>(
    protected val select: SelectStep<DEST_TYPE>,
    protected val propType: Select.PropType
) {

    abstract infix fun `as`(alias: KMutableProperty1<DEST_TYPE, SRC_PROP?>)

}

class AliasedPropertyBuilder<SRC_PROP, DEST_TYPE : Any>(
    select: SelectStep<DEST_TYPE>,
    propType: Select.PropType,
    private val src: KProperty1<*, SRC_PROP?>
) : PropertyBuilder<SRC_PROP, DEST_TYPE>(select, propType), Builder<AliasedProperty<SRC_PROP, DEST_TYPE>> {

    private lateinit var alias: KMutableProperty1<DEST_TYPE, SRC_PROP?>

    override fun `as`(alias: KMutableProperty1<DEST_TYPE, SRC_PROP?>) {
        this.alias = alias
        select.add(this)
    }

    override fun invoke(): AliasedProperty<SRC_PROP, DEST_TYPE> = AliasedProperty(propType, src, alias)

}

class ComputedPropertyBuilder<SRC_PROP_X, SRC_PROP_Y, SRC_PROP_Z, DEST_TYPE : Any>(
    select: SelectStep<DEST_TYPE>,
    propType: Select.PropType,
    private val srcX: KProperty1<*, SRC_PROP_X?>,
    private val srcY: KProperty1<*, SRC_PROP_Y?>,
    private val srcZ: (SRC_PROP_X?, SRC_PROP_Y?) -> SRC_PROP_Z?,
    private val descSrcZOperation: String
) : PropertyBuilder<SRC_PROP_Z, DEST_TYPE>(select, propType),
    Builder<ComputedProperty<SRC_PROP_X, SRC_PROP_Y, SRC_PROP_Z, DEST_TYPE>> {

    private lateinit var alias: KMutableProperty1<DEST_TYPE, SRC_PROP_Z?>

    override fun `as`(alias: KMutableProperty1<DEST_TYPE, SRC_PROP_Z?>) {
        this.alias = alias
        select.add(this)
    }

    override fun invoke(): ComputedProperty<SRC_PROP_X, SRC_PROP_Y, SRC_PROP_Z, DEST_TYPE> =
        ComputedProperty(propType, srcX, srcY, srcZ, descSrcZOperation, alias)

}