package top.viclau.magicbox.box.client.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.gson.*
import org.slf4j.LoggerFactory

fun GsonBuilder.initForHttpClient(): GsonBuilder = disableHtmlEscaping()

abstract class BaseHttpClient(
    requestTimeoutMillis: Long = DEFAULT_REQUEST_TIMEOUT_MILLIS,
    logContent: LogContent = DEFAULT_LOG_CONTENT
) : AutoCloseable {

    protected val log: org.slf4j.Logger = LoggerFactory.getLogger(this::class.java)

    protected val client: HttpClient
    protected val _gson: Gson = GsonBuilder().initForHttpClient().create()

    init {
        client = HttpClient(CIO) {
            install(HttpTimeout) {
                this.requestTimeoutMillis = requestTimeoutMillis
            }
            install(ContentNegotiation) {
                gson {
                    initForHttpClient()
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

    companion object {
        const val DEFAULT_REQUEST_TIMEOUT_MILLIS: Long = 10000
        val DEFAULT_LOG_CONTENT: LogContent = LogContent.NONE
    }

}

// hide implementation detail on ktor-client-logging
enum class LogContent(internal val logLevel: LogLevel) {
    ALL(LogLevel.ALL),
    HEADERS(LogLevel.HEADERS),
    BODY(LogLevel.BODY),
    INFO(LogLevel.INFO),
    NONE(LogLevel.NONE)
}
