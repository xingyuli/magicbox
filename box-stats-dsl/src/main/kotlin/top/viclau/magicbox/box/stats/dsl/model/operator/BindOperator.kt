/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.model.operator

import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import top.viclau.magicbox.box.stats.dsl.model.BaseProperty
import top.viclau.magicbox.box.stats.dsl.model.GroupByExtract
import top.viclau.magicbox.box.stats.dsl.model.Query
import kotlin.reflect.full.primaryConstructor

class BindOperator<DEST_TYPE : Any>(query: Query<DEST_TYPE>) :
    Operator<DEST_TYPE, BindOperator.In, List<DEST_TYPE>>(query) {

    data class In(val requests: List<QueryRequest>, val combinedMetrics: Collection<CombinedMetrics>)

    private val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

    override fun invoke(`in`: In): List<DEST_TYPE> {
        val dimensionValueToDestInstance: Map<DimensionValue, DEST_TYPE> =
            `in`.combinedMetrics.associate { it.dimensionValue to query.destTypeClass.primaryConstructor!!.call() }

        runBlocking {
            launch {
                bindMetricValues(`in`, dimensionValueToDestInstance)
            }
            launch {
                bindDimensionExtractValues(`in`, dimensionValueToDestInstance)
            }
        }

        return dimensionValueToDestInstance.values.toList()
    }

    private fun bindMetricValues(`in`: In, dimensionValueToDestInstance: Map<DimensionValue, DEST_TYPE>) {
        val reqSeqToReqs = `in`.requests.groupBy { it.id.seq }

        `in`.combinedMetrics.forEach { combined ->
            val destInstance = dimensionValueToDestInstance[combined.dimensionValue]!!

            val actualReqSeqToMetrics = combined.toSubMetrics().associateBy { it.reqSeq }

            reqSeqToReqs.forEach { (reqSeq, reqs) ->
                reqs.forEach { req ->
                    req.metrics.map { it.contributor }.toSet().forEach {
                        // TODO viclau t:performance p:lowest caching? different contributor may read same src metric value
                        (it as BaseProperty<*, DEST_TYPE>).bind(actualReqSeqToMetrics[reqSeq], destInstance)
                    }
                }
            }
        }
    }

    private suspend fun bindDimensionExtractValues(
        `in`: In,
        dimensionValueToDestInstance: Map<DimensionValue, DEST_TYPE>
    ) {
        val depthToDimensionValues: Map<Int, List<String>> = (0 until query.group.bys.size).associateWith { depth ->
            `in`.combinedMetrics.map { it.dimensionValue[depth] }.filter { it.isNotEmpty() }
        }

        val depthToDimensionRecords: Map<Int, List<Any>> = coroutineScope {
            depthToDimensionValues.mapValues { (depth, dimensionValues) ->
                async { query.group.bys[depth].dimensionRecordMapper(dimensionValues) }
            }.mapValues { (_, deferred) ->
                deferred.await()
            }
        }

        val depthToDimensionValueToRecords: Map<Int, Map<String, Any>> =
            depthToDimensionRecords.mapValues { (depth, dimensionRecords) ->
                val groupBy = query.group.bys[depth]
                dimensionRecords.associateBy { (groupBy.dimensionValue as (Any) -> Any?).invoke(it)!!.toString() }
            }

        `in`.combinedMetrics.forEach { combined ->
            val destInstance = dimensionValueToDestInstance[combined.dimensionValue]!!

            query.group.bys.forEachIndexed { depth, groupBy ->
                val dimensionValueOfThisGroupBy = combined.dimensionValue[depth]
                val dimensionRecord = depthToDimensionValueToRecords[depth]!![dimensionValueOfThisGroupBy]

                dimensionRecord?.let { record ->
                    groupBy.extracts.forEach { extract ->
                        (extract as GroupByExtract<Any, *, DEST_TYPE>).bind(record, destInstance)
                    }
                } ?: log.info(
                    "dimension record is not found: depth = $depth, value = $dimensionValueOfThisGroupBy\ngroupBy.column = ${groupBy.column}\nCombinedMetrics = {}",
                    gson.toJson(combined)
                )
            }
        }

    }

}