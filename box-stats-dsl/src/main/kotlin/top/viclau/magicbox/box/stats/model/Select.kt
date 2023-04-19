/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model

import top.viclau.magicbox.box.stats.ext.ownerType
import top.viclau.magicbox.box.stats.model.operator.SubCombinedMetrics
import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

class Select<DEST_TYPE> {

    private val aliasedProperties = mutableListOf<AliasedProperty<*, DEST_TYPE>>()
    private val computedProperties = mutableListOf<ComputedProperty<*, *, *, DEST_TYPE>>()

    enum class PropType {
        NORMAL,
        RATE
    }

    fun add(aliasedProperty: AliasedProperty<*, DEST_TYPE>) {
        aliasedProperties.add(aliasedProperty)
    }

    fun add(computedProperty: ComputedProperty<*, *, *, DEST_TYPE>) {
        computedProperties.add(computedProperty)
    }

    fun visit(visitor: Visitor<DEST_TYPE>) {
        aliasedProperties.forEach { visitor.visitAliasedProp(it) }
        computedProperties.forEach { visitor.visitComputedProp(it) }
    }

    open class Visitor<DEST_TYPE> {

        open fun visitAliasedProp(prop: AliasedProperty<*, DEST_TYPE>) {
            // default to no-op
        }

        open fun visitComputedProp(prop: ComputedProperty<*, *, *, DEST_TYPE>) {
            // default to no-op
        }

    }


}

sealed class BaseProperty<SRC_PROP, DEST_TYPE>(
    private val propType: Select.PropType,
    private val alias: KMutableProperty1<DEST_TYPE, SRC_PROP?>
) {
//    val isAvg: Boolean
//        get() = Select.PropType.AVG === propType
//    val isRate: Boolean
//        get() = Select.PropType.RATE === propType
//    val isPercentageString: Boolean
//        get() = Select.PropType.PERCENTAGE_STRING === propType

    fun bind(subCombinedMetrics: SubCombinedMetrics?, target: DEST_TYPE) {
        alias.set(target, resolveAliasValue(subCombinedMetrics))
    }

    protected abstract fun resolveAliasValue(subCombinedMetrics: SubCombinedMetrics?): SRC_PROP?

    protected fun <V> KProperty1<*, V?>.resolveSrcValue(subCombinedMetrics: SubCombinedMetrics?): V {
        @Suppress("UNCHECKED_CAST")
        val prop = this as KProperty1<Any, V?>
        val srcObj = subCombinedMetrics?.get(ownerType()!!)
        return srcObj?.let { prop(it) } ?: default()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <V> KProperty1<*, V?>.default(): V =
        when (this.returnType.classifier!! as KClass<*>) {
            Int::class -> 0 as V
            Long::class -> 0L as V
            Double::class -> 0.0 as V
            BigDecimal::class -> BigDecimal.ZERO as V
            String::class -> "" as V
            else -> throw RuntimeException("unsupported metric property type: $this")
        }

}

class AliasedProperty<SRC_PROP, DEST_TYPE>(
    propType: Select.PropType,
    val src: KProperty1<*, SRC_PROP?>,
    alias: KMutableProperty1<DEST_TYPE, SRC_PROP?>
) : BaseProperty<SRC_PROP, DEST_TYPE>(propType, alias) {
    override fun resolveAliasValue(subCombinedMetrics: SubCombinedMetrics?): SRC_PROP {
        return src.resolveSrcValue(subCombinedMetrics)
    }
}

class ComputedProperty<SRC_PROP_X, SRC_PROP_Y, SRC_PROP_Z, DEST_TYPE>(
    propType: Select.PropType,
    val srcX: KProperty1<*, SRC_PROP_X?>,
    val srcY: KProperty1<*, SRC_PROP_Y?>,
    val srcZ: (SRC_PROP_X?, SRC_PROP_Y?) -> SRC_PROP_Z?,
    val descSrcZOperation: String,
    alias: KMutableProperty1<DEST_TYPE, SRC_PROP_Z?>
) : BaseProperty<SRC_PROP_Z?, DEST_TYPE>(propType, alias) {
    override fun resolveAliasValue(subCombinedMetrics: SubCombinedMetrics?): SRC_PROP_Z? {
        val valueX = srcX.resolveSrcValue(subCombinedMetrics)
        val valueY = srcY.resolveSrcValue(subCombinedMetrics)
        return srcZ(valueX, valueY)
    }
}
