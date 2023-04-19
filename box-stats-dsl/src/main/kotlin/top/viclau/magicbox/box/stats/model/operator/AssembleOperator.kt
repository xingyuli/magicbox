/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model.operator

import top.viclau.magicbox.box.stats.ext.ownerType
import top.viclau.magicbox.box.stats.model.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

data class QueryRequest(
    val id: ReqId,
    val dataset: KClass<*>,
    val where: Where,
    val metrics: MutableList<MetricRequest> = mutableListOf()
) {

    fun addMetric(m: MetricRequest) {
        metrics.add(m)
    }

    fun metricProps(): Set<KProperty1<*, *>> = metrics.map { it.prop }.toSet()

}

data class ReqId(val seq: Int, val source: String)

data class MetricRequest(val prop: KProperty1<*, *>, val contributor: BaseProperty<*, *>)

class AssembleOperator<DEST_TYPE : Any>(query: Query<DEST_TYPE>) :
    Operator<DEST_TYPE, Unit, List<QueryRequest>>(query) {

    override fun invoke(ignore: Unit): List<QueryRequest> {
        val result = mutableListOf<QueryRequest>()

        query.selectWheres.forEachIndexed { i, sw ->
            val localRequests = mutableMapOf<KClass<*>, QueryRequest>()
            sw.select.visit(object : Select.Visitor<DEST_TYPE>() {

                override fun visitAliasedProp(prop: AliasedProperty<*, DEST_TYPE>) {
                    addMetric(prop.src, prop)
                }

                override fun visitComputedProp(prop: ComputedProperty<*, *, *, DEST_TYPE>) {
                    addMetric(prop.srcX, prop)
                    addMetric(prop.srcY, prop)
                }

                private fun addMetric(prop: KProperty1<*, *>, contributor: BaseProperty<*, *>) {
                    val ownerType = prop.ownerType()!!
                    localRequests.computeIfAbsent(ownerType) {
                        QueryRequest(
                            ReqId(i, ownerType.simpleName!!),
                            ownerType,
                            sw.where
                        )
                    }.addMetric(MetricRequest(prop, contributor))
                }

            })

            result.addAll(localRequests.values)
        }

        return result
    }

}