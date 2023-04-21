package top.viclau.magicbox.box.stats.dsl.plugin.superset

import top.viclau.magicbox.box.stats.model.QueryDialect
import kotlin.reflect.KClass

object SupersetDialect : QueryDialect<SupersetQueryEngine, SupersetDialectScope, SupersetWhere> {

    override val engineType: KClass<SupersetQueryEngine> get() = SupersetQueryEngine::class

    override fun invoke(): SupersetDialectScope = SupersetDialectScope()

    override fun toModel(scope: SupersetDialectScope): SupersetWhere =
        SupersetWhere(scope.timeRange, scope.conditionalBuilders.values.flatMap { it.mapNotNull { it() } })

}
