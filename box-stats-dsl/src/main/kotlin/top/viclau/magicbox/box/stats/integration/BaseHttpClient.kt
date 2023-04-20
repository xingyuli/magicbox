package top.viclau.magicbox.box.stats.integration

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.gson.*
import org.slf4j.LoggerFactory

internal fun GsonBuilder.init() = disableHtmlEscaping()

abstract class BaseHttpClient(logContent: LogContent = LogContent.NONE) : AutoCloseable {

    protected val log: org.slf4j.Logger = LoggerFactory.getLogger(this::class.java)

    // TODO viclau - robustness - ktor client timeout setting
    protected val client: HttpClient
    protected val _gson: Gson = GsonBuilder().init().create()

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

}

// hide implementation detail on ktor-client-logging
enum class LogContent(internal val logLevel: LogLevel) {
    ALL(LogLevel.ALL),
    HEADERS(LogLevel.HEADERS),
    BODY(LogLevel.BODY),
    INFO(LogLevel.INFO),
    NONE(LogLevel.NONE)
}
