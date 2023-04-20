package top.viclau.magicbox.box.stats.integration.ladder

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import top.viclau.magicbox.box.stats.integration.BaseHttpClient
import top.viclau.magicbox.box.stats.integration.LogContent
import top.viclau.magicbox.box.stats.integration.ladder.qt.GetDetailResponseData
import top.viclau.magicbox.box.stats.integration.ladder.qt.QueryDataRequest
import top.viclau.magicbox.box.stats.integration.ladder.user.LoginRequest

class LadderClient(private val config: Config, logContent: LogContent = LogContent.NONE) : BaseHttpClient(logContent) {

    private val token: String by lazy {
        runBlocking { userApi().login() }.token
    }

    data class Config(
        /**
         * In shape of: `http|https://host/api`
         */
        val endpoint: String, val username: String, val password: String
    )

    private fun userApi() = UserApi()

    fun queryTemplateApi() = QueryTemplateApi()

    inner class UserApi internal constructor() {

        private val base = "${config.endpoint}/user"

        suspend fun login(): LoginRequest.ResponseData {
            val response = client.post("${base}/login") {
                contentType(ContentType.Application.Json)

                setBody(LoginRequest(username = config.username, password = config.password))
            }
            return response.toResponseData()
        }

    }

    inner class QueryTemplateApi internal constructor() {

        private val base = "${config.endpoint}/qt"

        suspend fun getDetail(id: Long): GetDetailResponseData {
            // TODO viclau remove duplication for token setting
            val response = client.get("${base}/detail") {
                bearerAuth(token)

                url {
                    parameters.append("id", id.toString())
                }
            }
            return response.toResponseData()
        }

        suspend fun queryData(request: QueryDataRequest, requestId: String? = null): QueryDataRequest.ResponseData {
            val response = client.post("${base}/query_data") {
                bearerAuth(token)

                contentType(ContentType.Application.Json)
                log.info("{} : {}", requestId ?: "(unspecified request id)", _gson.toJson(request))

                setBody(request)
            }

            // TODO log.info 查询成功

            return response.toResponseData()
        }

    }

    private suspend inline fun <reified T> HttpResponse.toResponseData(): T {
        // TODO viclau use custom exception
        if (status != HttpStatusCode.OK) {
            throw RuntimeException("http status is $status, with response body: ${bodyAsText()}")
        }

        val ladderResponse = body<LadderResponse<T>>()

        return ladderResponse.takeIf { it.isSuccess }?.data
            ?: throw RuntimeException("http status is $status, but application response data implies a failure: ${ladderResponse.message}")
    }

}
