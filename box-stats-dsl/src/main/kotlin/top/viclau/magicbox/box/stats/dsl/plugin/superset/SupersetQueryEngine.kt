package top.viclau.magicbox.box.stats.dsl.plugin.superset

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import top.viclau.magicbox.box.client.http.superset.SupersetClient
import top.viclau.magicbox.box.client.http.superset.chart.data.Datasource
import top.viclau.magicbox.box.client.http.superset.chart.data.Queries
import top.viclau.magicbox.box.client.http.superset.chart.data.QueryDataRequest
import top.viclau.magicbox.box.stats.ext.superset.queryData
import top.viclau.magicbox.box.stats.model.QueryEngine
import top.viclau.magicbox.box.stats.model.operator.QueryRequest
import top.viclau.magicbox.box.stats.model.operator.QueryResponse
import top.viclau.magicbox.box.stats.model.support.DatasetResolver

class SupersetQueryEngine(config: Config) : QueryEngine<SupersetQueryEngine>(config) {

    data class Config(val clientConfig: SupersetClient.Config) : QueryEngine.Config<SupersetQueryEngine>

    private val client = SupersetClient(config.clientConfig)

    override suspend fun execute(requests: List<QueryRequest>) = coroutineScope {
        client.use {
            // TODO viclau t:core p:low - performance - how to control coroutines' concurrency level?
            requests.map { req ->
                async { executeSingleRequest(req) }
            }.map { it.await() }
        }
    }

    private suspend fun executeSingleRequest(req: QueryRequest): QueryResponse {
        val datasource = Datasource.table(DatasetResolver.resolveId(req.dataset, req).toInt())

        val queries = Queries()

        val extras = JsonObject()

        val where = req.where as SupersetWhere
        where.timeRange?.let {
            // for example: 2023-03-11 09:56:37 : 2023-04-11 09:56:37
            queries.time_range = it()
            queries.granularity = it.column

            extras.add("time_range_endpoints", JsonArray().apply {
                add("inclusive")
                add(if (it.includeEnd) "inclusive" else "exclusive")
            })
        }

        with(where.filters.partition { it is ChartDataFilterShim }) {
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

        val groupByColumnNames = req.group.bys.map { it.column.name }
        queries.columns = groupByColumnNames
        queries.groupby = groupByColumnNames
        queries.metrics = req.metricProps().map { it.name }
        queries.extras = extras

        // TODO viclau t:engine p:low - `row_limit` is hard coded `10000`, should be configurable
        queries.row_limit = 10000

        val responseData = client.chartApi().queryData(
            // TODO viclau t:engine p:low - `force` is hard coded to `true`, should be configurable
            QueryDataRequest(datasource = datasource, force = true, queries = listOf(queries)),
            req.dataset,
            requestId = req.id.toString()
        )

        return QueryResponse(req, responseData)
    }

}
