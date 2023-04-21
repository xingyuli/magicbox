/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model.operator

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import top.viclau.magicbox.box.stats.model.Query
import kotlin.reflect.full.primaryConstructor

data class QueryResponse(
    val request: QueryRequest,
    val result: List<Any>
)

class QueryOperator<DEST_TYPE : Any>(query: Query<DEST_TYPE>) :
    Operator<DEST_TYPE, List<QueryRequest>, List<QueryResponse>>(query) {

    override fun invoke(requests: List<QueryRequest>): List<QueryResponse> {
        val dialectToRequests = requests.groupBy { it.where.dialect }

        val responses = runBlocking {
            val dialectResponses: List<List<QueryResponse>> = dialectToRequests.map { (dialect, requests) ->
                val engineConfig = query.config.getEngineConfigByEngineType(dialect.engineType)!!
                val engine = dialect.engineType.primaryConstructor!!.call(engineConfig)
                async { engine.execute(requests) }
            }.map { it.await() }

            val responses = mutableListOf<QueryResponse>()
            dialectResponses.forEach {
                responses.addAll(it)
            }
            return@runBlocking responses
        }

        return responses
    }

}
