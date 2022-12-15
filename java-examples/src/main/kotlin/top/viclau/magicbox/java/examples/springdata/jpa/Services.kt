/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.springdata.jpa


interface QueryRequest {
    val scope: Scoped.Scope?
    val scopeValue: String?
    fun <T> param(parameterName: String): T?
}

interface QueryResponse {
    val error: Exception?
}

interface QueryContext {
    val request: QueryRequest
    val response: QueryResponse
}

interface CRMService {
    fun addEmployee(context: QueryContext)
}