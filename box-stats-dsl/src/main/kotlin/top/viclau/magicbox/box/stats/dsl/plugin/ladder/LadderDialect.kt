package top.viclau.magicbox.box.stats.dsl.plugin.ladder

import top.viclau.magicbox.box.stats.model.DialectScope
import top.viclau.magicbox.box.stats.model.QueryDialect
import top.viclau.magicbox.box.stats.model.Where
import kotlin.reflect.KClass

class LadderWhere : Where(LadderDialect)

class LadderDialectScope : DialectScope

object LadderDialect : QueryDialect<LadderQueryEngine, LadderDialectScope, LadderWhere> {

    override val engineType: KClass<LadderQueryEngine>
        get() = LadderQueryEngine::class

    override fun invoke(): LadderDialectScope {
        TODO("Not yet implemented")
    }

    override fun toModel(scope: LadderDialectScope): LadderWhere {
        TODO("Not yet implemented")
    }

}
