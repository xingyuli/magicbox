package top.viclau.magicbox.box.stats.ext.superset

import com.google.gson.FieldNamingPolicy
import com.google.gson.FieldNamingStrategy
import com.google.gson.GsonBuilder
import top.viclau.magicbox.box.stats.ext.StringCase
import top.viclau.magicbox.box.stats.ext.snakeToCamelCase
import top.viclau.magicbox.box.stats.integration.superset.SupersetClient
import top.viclau.magicbox.box.stats.integration.superset.chart.data.ChartDataFilter
import top.viclau.magicbox.box.stats.integration.superset.chart.data.Datasource
import top.viclau.magicbox.box.stats.integration.superset.chart.data.Queries
import top.viclau.magicbox.box.stats.integration.superset.chart.data.QueryDataRequest
import top.viclau.magicbox.box.stats.model.support.DatasetResolver
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

internal fun GsonBuilder.init() = disableHtmlEscaping()

private val gsonFactory = StringCase.values().associateWith {
    GsonBuilder().apply {
        init()
        setFieldNamingStrategy(it.gsonFieldNamingStrategy)
    }.create()
}

private val StringCase.gsonFieldNamingStrategy: FieldNamingStrategy
    get() = when (this) {
        StringCase.IDENTITY -> FieldNamingPolicy.IDENTITY
        StringCase.CAMEL_TO_SNAKE -> FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
        StringCase.SNAKE_TO_CAMEL -> FieldNamingStrategy { it.name.snakeToCamelCase() }
    }

suspend fun <T : Any> SupersetClient.ChartApi.queryData(
    request: QueryDataRequest,
    resultType: KClass<T>,
    requestId: String? = null
): List<T> {
    val response = queryData(request, requestId)
    return response.result[0].data.map { gsonFactory[StringCase.IDENTITY]!!.fromJson(it, resultType.java) }
}

suspend fun <T : Any> SupersetClient.ChartApi.queryTable(
    dataset: KClass<T>,
    filters: List<ChartDataFilter>,
    namingCase: StringCase = StringCase.IDENTITY,
    force: Boolean = true,
    requestId: String? = null
): List<T> {
    val datasource = Datasource.table(DatasetResolver.resolveId(dataset, null).toInt())

    val queries = Queries().apply {
        columns = dataset.members.filterIsInstance<KProperty<*>>().map { namingCase(it.name) }
        this.filters = filters
    }

    val request = QueryDataRequest(datasource = datasource, force = force, queries = listOf(queries))

    val response = queryData(request, requestId)
    return response.result[0].data.map { gsonFactory[namingCase]!!.fromJson(it, dataset.java) }
}
