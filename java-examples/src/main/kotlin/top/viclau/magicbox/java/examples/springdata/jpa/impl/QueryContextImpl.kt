/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.springdata.jpa.impl

import top.viclau.magicbox.java.examples.springdata.jpa.QueryContext
import top.viclau.magicbox.java.examples.springdata.jpa.QueryRequest
import top.viclau.magicbox.java.examples.springdata.jpa.QueryResponse

class QueryContextImpl(request: QueryRequest) : QueryContext {

    private val _request: QueryRequest
    private lateinit var _response: QueryResponse

    init {
        this._request = request
    }

    override val request: QueryRequest
        get() = _request

    override var response: QueryResponse
        get() = _response
        set(value) {
            _response = value
        }

}