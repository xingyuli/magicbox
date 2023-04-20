package top.viclau.magicbox.box.stats.engine.ladder

import top.viclau.magicbox.box.stats.engine.QueryEngine
import top.viclau.magicbox.box.stats.engine.QueryRequest
import top.viclau.magicbox.box.client.http.ladder.LadderClient

class LadderQueryEngine(config: Config) : QueryEngine<LadderQueryEngine>(config) {

    data class Config(val clientConfig: LadderClient.Config) : QueryEngine.Config<LadderQueryEngine>

    override fun execute(req: QueryRequest) {
        TODO("Not yet implemented")
    }

}