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