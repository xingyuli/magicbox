/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.model.operator

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import top.viclau.magicbox.box.stats.dsl.ext.superset.queryData
import top.viclau.magicbox.box.stats.dsl.model.*
import top.viclau.magicbox.box.stats.dsl.model.ChartDataFilterShim
import top.viclau.magicbox.box.stats.dsl.model.superset.chart.data.Datasource
import top.viclau.magicbox.box.stats.dsl.model.superset.chart.data.Queries
import top.viclau.magicbox.box.stats.dsl.model.superset.chart.data.QueryDataRequest
import top.viclau.magicbox.box.stats.dsl.model.superset.api.SupersetClient
import top.viclau.magicbox.box.stats.dsl.support.DatasetResolver

data class QueryResponse(
    val request: QueryRequest,
    val result: List<Any>
)

// TODO viclau t:core p:high - QueryEngine - refactoring: class hierarchy SupersetQueryOperator ..-> QueryOperator
class QueryOperator<DEST_TYPE : Any>(query: Query<DEST_TYPE>) :
    Operator<DEST_TYPE, List<QueryRequest>, List<QueryResponse>>(query) {

    private val client = SupersetClient(query.config.supersetConfig)

    override fun invoke(requests: List<QueryRequest>): List<QueryResponse> {
        return client.use {
            // TODO viclau t:core p:low - performance - how to control coroutines' concurrency level?
            runBlocking {
                requests.map { req ->
                    async { executeSingleRequest(req) }
                }.map { it.await() }
            }
        }
    }

    private suspend fun executeSingleRequest(req: QueryRequest): QueryResponse {
        val datasource = Datasource.table(DatasetResolver.resolveId(req.dataset, query).toInt())

        val queries = Queries()

        val extras = JsonObject()

        // TODO viclau t:engine p:low - customizable request protocol - superset only, time range is mandatory
        req.where.timeRange?.let {
            // for example: 2023-03-11 09:56:37 : 2023-04-11 09:56:37
            queries.time_range = it()
            queries.granularity = it.column

            extras.add("time_range_endpoints", JsonArray().apply {
                add("inclusive")
                add(if (it.includeEnd) "inclusive" else "exclusive")
            })
        }

        with(req.where.filters.partition { it is ChartDataFilterShim }) {
            val (shimmedFilters, unShimmedFilters) = this

            // 尽可能使用 superset 本身支持的 filter
            queries.filters = shimmedFilters.map { (it as ChartDataFilterShim).toChartDataFilter() }

            // 对于 superset 不支持的 查询条件, 使用自定义 sql
            unShimmedFilters
                .map { it() }
                .takeIf { it.isNotEmpty() }
                ?.let {
                    extras.addProperty("where", it.joinToString(" AND "))
                }
        }

        val groupByColumnNames = query.group.bys.map { it.column.name }
        queries.columns = groupByColumnNames
        queries.groupby = groupByColumnNames
        queries.metrics = req.metricProps().map { it.name }
        queries.extras = extras

        // TODO viclau t:engine p:low - customizable request protocol - superset only, solve the row_limit problem, support it via QueryConfig ?
        queries.row_limit = 10000

        val responseData = client.chartApi().queryData(
            // TODO viclau t:engine p:low - customizable request protocol - superset only, the `force` parameter could be configured externally
            QueryDataRequest(datasource = datasource, force = true, queries = listOf(queries)),
            req.dataset,
            requestId = req.id.toString()
        )

        return QueryResponse(req, responseData)
    }

}