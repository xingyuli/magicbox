package top.viclau.magicbox.box.stats.integration.ladder.qt

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class GetDetailResponseData(val id: Long, val name: String, val sql: String, val params: JsonArray)

data class QueryDataRequest(val id: String, val params: JsonObject) {
    data class ResponseData(val colnames: List<String>, val records: List<JsonObject>, val rowcount: Int)
}
