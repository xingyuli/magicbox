package top.viclau.magicbox.box.stats.engine

// TODO viclau name conflicts against `top.viclau.magicbox.box.stats.dsl.model.operator.QueryRequest`
class QueryRequest

abstract class QueryEngine<T : QueryEngine<T>>(private val config: Config<T>) {

    abstract fun execute(req: QueryRequest)

    interface Config<T : QueryEngine<T>>

}
