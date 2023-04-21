package top.viclau.magicbox.box.stats.dsl.plugin.ladder

import top.viclau.magicbox.box.client.http.ladder.LadderClient
import top.viclau.magicbox.box.stats.model.QueryEngine
import top.viclau.magicbox.box.stats.model.operator.QueryRequest
import top.viclau.magicbox.box.stats.model.operator.QueryResponse

class LadderQueryEngine(config: Config) : QueryEngine<LadderQueryEngine>(config) {

    data class Config(val clientConfig: LadderClient.Config) : QueryEngine.Config<LadderQueryEngine>

    override suspend fun execute(requests: List<QueryRequest>): List<QueryResponse> {
        TODO("Not yet implemented")
    }

}