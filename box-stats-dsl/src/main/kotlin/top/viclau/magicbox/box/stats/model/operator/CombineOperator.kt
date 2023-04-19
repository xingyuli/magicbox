/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model.operator

import top.viclau.magicbox.box.stats.model.Query
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

typealias DimensionValue = List<String>

class CombinedMetrics(val dimensionValue: DimensionValue) {

    // value of the map: an instance of Metric class
    private val metrics: MutableMap<ReqId, Any> = mutableMapOf()

    fun accept(reqId: ReqId, m: Any) {
        metrics[reqId] = m
    }

    fun toSubMetrics(): List<SubCombinedMetrics> {
        return metrics.entries
            .groupBy { (reqId, _) -> reqId.seq }
            .map { (reqSeq, subCombinedMetrics) ->
                SubCombinedMetrics(dimensionValue, subCombinedMetrics.associate { it.key to it.value }, reqSeq)
            }
    }

}

class SubCombinedMetrics internal constructor(
    val dimensionValue: DimensionValue,
    private val metrics: Map<ReqId, Any>,
    val reqSeq: Int
) {
    operator fun get(metricSource: KClass<*>): Any? = metrics[ReqId(reqSeq, metricSource.simpleName!!)]
}

class CombineOperator<DEST_TYPE : Any>(query: Query<DEST_TYPE>) :
    Operator<DEST_TYPE, List<QueryResponse>, Collection<CombinedMetrics>>(query) {

    override fun invoke(responses: List<QueryResponse>): Collection<CombinedMetrics> {
        val dimensionValueGetters = query.group.bys.map { it.column as KProperty1<Any, *> }
        val dimensionValues: Set<DimensionValue> = responses.flatMap { (_, metricsFromOneDataset) ->
            metricsFromOneDataset.map { row ->
                dimensionValueGetters.map { it.get(row)?.toString() ?: "" }
            }
        }.toSet()

        // `dimensionValues` is collected from different datasets, this indicates metric value exist in at least one
        // dataset.
        //
        // Init with empty metrics for simplifying the collection manipulation.
        val dimensionValueToCombinedMetrics = dimensionValues.associateWith { CombinedMetrics(it) }

        responses.forEach { (req, metricsFromOneDataset) ->
            metricsFromOneDataset.forEach { row ->
                val dimensionValue = dimensionValueGetters.map { it.get(row)?.toString() ?: "" }
                val combinedMetrics = dimensionValueToCombinedMetrics[dimensionValue]!!
                combinedMetrics.accept(req.id, row)
            }
        }

        return dimensionValueToCombinedMetrics.values
    }

}