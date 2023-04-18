/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.model.superset.chart.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject


data class QueryDataRequest(
    val datasource: Datasource,
    val queries: List<Queries>,
    val result_format: ResultFormat = ResultFormat.json,
    val result_type: ResultType = ResultType.full,
    val force: Boolean = false,
) {
    data class Response(val result: List<ResponseResult>)
}

data class Datasource(val id: Int, val type: String) {
    companion object {
        fun table(id: Int) = Datasource(id, type = "table")
    }
}

data class Queries(
    var annotation_layers: List<AnnotationLayer>? = null,
    var applied_time_extras: JsonObject? = null,
    var columns: List<String>? = null,
    var druid_time_origin: String? = null,
    var extras: JsonObject? = null,
    var filters: List<ChartDataFilter>? = null,
    var granularity: String? = null,
    var granularity_sqla: String? = null,
    var groupby: List<String>? = null,
    var having: String? = null,
    var having_filters: List<ChartDataFilter>? = null,
    var is_timeseries: Boolean? = null,
    // TODO viclau t:metric p:low - newer superset (since which version?) supports ad-hoc metrics
    var metrics: List<String>? = null,
    var order_desc: Boolean? = false,
    var orderby: List<List<Any>>? = null,
    var post_processing: List<JsonObject>? = null,
    var row_limit: Int? = null,
    var row_offset: Int? = 0,
    var time_range: String? = null,
    var time_shift: String? = null,
    var timeseries_limit: Int? = 0,
    var url_params: JsonObject? = null,
    var where: String? = null,
)

data class AnnotationLayer(
    val annotationType: String,
    val color: String? = null,
    val descriptionColumns: List<String>? = null,
    val hideLine: Boolean? = false,
    val intervalEndColumn: String? = null,
    val name: String,
    val opacity: String? = null,
    val overrides: JsonObject? = null,
    val show: Boolean,
    val showMarkers: Boolean,
    val sourceType: String,
    val style: String,
    val timeColumn: String? = null,
    val titleColumn: String? = null,
    val value: JsonObject,
    val width: Int = 0,
)

enum class ResultFormat {
    json, csv
}

enum class ResultType {
    full, query
}

data class ResponseResult(val data: JsonArray)
