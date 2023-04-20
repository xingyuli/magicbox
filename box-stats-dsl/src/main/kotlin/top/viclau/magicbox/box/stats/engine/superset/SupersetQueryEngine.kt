package top.viclau.magicbox.box.stats.engine.superset

import top.viclau.magicbox.box.client.http.superset.SupersetClient
import top.viclau.magicbox.box.stats.engine.QueryEngine
import top.viclau.magicbox.box.stats.engine.QueryRequest

class SupersetQueryEngine(config: Config) : QueryEngine<SupersetQueryEngine>(config) {

    data class Config(val clientConfig: SupersetClient.Config) : QueryEngine.Config<SupersetQueryEngine>

    override fun execute(req: QueryRequest) {
        TODO("Not yet implemented")
    }

}
