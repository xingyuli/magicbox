/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.integration.superset

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import top.viclau.magicbox.box.stats.integration.BaseHttpClient
import top.viclau.magicbox.box.stats.integration.LogContent
import top.viclau.magicbox.box.stats.integration.superset.chart.data.QueryDataRequest
import top.viclau.magicbox.box.stats.integration.superset.security.LoginRequest
import java.util.concurrent.ConcurrentHashMap

class SupersetClient(private val config: Config, logContent: LogContent = LogContent.NONE) :
    BaseHttpClient(logContent) {

    // TODO add module box-common: LoadOnFailureCache
    private val accessToken: String by lazy {
        runBlocking { securityApi().login() }.access_token
    }

    data class Config(
        /**
         * In shape of: `http|https://host/api/v1`
         */
        val endpoint: String, val username: String, val password: String, val provider: String = "db"
    )

    private fun securityApi() = SecurityApi()

    fun chartApi() = ChartApi()

    companion object {

        // TODO remove this?
        private val factory = ConcurrentHashMap<Config, SupersetClient>()

    }

    inner class SecurityApi internal constructor() {

        private val base = "${config.endpoint}/security"

        suspend fun login(refresh: Boolean = false): LoginRequest.Response {
            // TODO refactoring - remove duplication
            val response = client.post("${base}/login") {
                contentType(ContentType.Application.Json)

                setBody(
                    LoginRequest(
                        username = config.username,
                        password = config.password,
                        provider = config.provider,
                        refresh = refresh,
                    )
                )
            }

            return response.body<LoginRequest.Response>()
        }

        suspend fun refresh() {
            // TODO implement refresh
        }

    }

    inner class ChartApi internal constructor() {

        private val base = "${config.endpoint}/chart"

        suspend fun queryData(request: QueryDataRequest, requestId: String? = null): QueryDataRequest.Response {
            val response = client.post("${base}/data") {
                bearerAuth(accessToken)

                contentType(ContentType.Application.Json)
                log.info("{} : {}", requestId ?: "(unspecified request id)", _gson.toJson(request))

                // TODO viclau t:hotdata p:low - record time column histogram

                setBody(request)
            }

            // TODO log.info 查询成功
//        log.info(
//            "superset数据查询成功 {}(value={}) id={}，耗时: {} ms",
//            datasetRef.getClass().getSimpleName(),
//            datasetRef.getValue(),
//            dataset.getId(),
//            System.currentTimeMillis() - startTime
//        )

            if (response.status != HttpStatusCode.OK) {
                // TODO fallback support when failure (including timed out)
                // TODO viclau use custom exception
                throw RuntimeException(response.bodyAsText())
            }

            return response.body<QueryDataRequest.Response>()
        }

    }

}
