/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model

import top.viclau.magicbox.box.stats.dsl.Builder
import top.viclau.magicbox.box.stats.model.operator.QueryRequest
import top.viclau.magicbox.box.stats.model.operator.QueryResponse
import kotlin.reflect.KClass

abstract class QueryEngine<T : QueryEngine<T>>(val config: Config<T>) {

    // TODO viclau t:core p:high - now a QueryEngine wraps a client, thus more performant to execute a batch of requests
    //  once the implementation can execute a single QueryRequest we can change the signature to `execute(request:
    //  QueryRequest)`
    abstract suspend fun execute(requests: List<QueryRequest>): List<QueryResponse>

    interface Config<T : QueryEngine<T>>

}

interface DialectScope

abstract class Where(val dialect: QueryDialect<*, *, *>)

interface QueryDialect<E : QueryEngine<*>, S : DialectScope, out W : Where> : Builder<S> {
    val engineType: KClass<E>
    fun toModel(scope: S): W
}
