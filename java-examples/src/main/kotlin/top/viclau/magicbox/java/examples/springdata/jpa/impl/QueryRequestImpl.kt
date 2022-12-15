/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.springdata.jpa.impl

import top.viclau.magicbox.java.examples.springdata.jpa.QueryRequest
import top.viclau.magicbox.java.examples.springdata.jpa.Scoped
import java.util.*

class QueryRequestImpl : QueryRequest {

    private var _scope: Scoped.Scope? = null
    private var _scopeValue: String? = null
    private val parameters: MutableMap<String, Any> = HashMap()

    override val scope: Scoped.Scope?
        get() = _scope

    override val scopeValue: String?
        get() = _scopeValue

    override fun <T> param(parameterName: String): T? = parameters[parameterName] as? T

    fun scope(scope: Scoped.Scope, scopeValue: String): QueryRequestImpl {
        _scope = scope
        _scopeValue = scopeValue
        return this
    }

    fun param(parameterName: String, parameterValue: Any): QueryRequestImpl {
        parameters[parameterName] = parameterValue
        return this
    }

    override fun toString(): String {
        return "{scope=${scope}, scopeValue=${scopeValue}, parameters=${parameters}}"
    }

}