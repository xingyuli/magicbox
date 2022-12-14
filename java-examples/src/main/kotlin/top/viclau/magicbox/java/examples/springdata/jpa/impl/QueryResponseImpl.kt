package top.viclau.magicbox.java.examples.springdata.jpa.impl

import top.viclau.magicbox.java.examples.springdata.jpa.QueryResponse

class QueryResponseImpl : QueryResponse {

    private var _error: Exception? = null

    override val error: Exception?
        get() = _error

    fun error(error: Exception): QueryResponseImpl {
        _error = error
        return this
    }

}
