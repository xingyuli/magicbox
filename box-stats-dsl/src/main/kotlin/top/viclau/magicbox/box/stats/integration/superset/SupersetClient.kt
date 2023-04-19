/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.integration.superset

import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import top.viclau.magicbox.box.stats.ext.superset.init
import top.viclau.magicbox.box.stats.integration.superset.chart.data.QueryDataRequest
import top.viclau.magicbox.box.stats.integration.superset.security.LoginRequest
import java.util.concurrent.ConcurrentHashMap

// hide implementation detail on ktor-client-logging
enum class LogContent(internal val logLevel: LogLevel) {
    ALL(LogLevel.ALL),
    HEADERS(LogLevel.HEADERS),
    BODY(LogLevel.BODY),
    INFO(LogLevel.INFO),
    NONE(LogLevel.NONE)
}

class SupersetClient(private val config: Config, logContent: LogContent = LogContent.NONE) : AutoCloseable {

    private val log: org.slf4j.Logger = LoggerFactory.getLogger(SupersetClient::class.java)
    private val _gson = GsonBuilder().init().create()

    // TODO viclau - robustness - ktor client timeout setting
    private val client: HttpClient

    // TODO add module box-common: LoadOnFailureCache
    private val accessToken: String by lazy {
        runBlocking { securityApi().login() }.access_token
    }

    init {
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson {
                    init()
                }
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = logContent.logLevel
            }
        }
    }

    override fun close() {
        client.close()
    }

    /**
     * endpoint:
     *   http|https://host/api/v1
     */
    data class Config(val endpoint: String, val username: String, val password: String, val provider: String = "db")

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
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }

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
                // TODO use custom exception (and, superset client should be placed in dedicated package)
                throw RuntimeException(response.bodyAsText())
            }

            return response.body<QueryDataRequest.Response>()
        }

    }

}
