package io.kort.inbooks.data.source

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Suppress("FunctionName")
fun AppHttpClient(
    engine: HttpClientEngine? = null,
    json: Json = DefaultJson,
    block: HttpClientConfig<*>.() -> Unit = {}
): HttpClient {
    val defaultBlock: HttpClientConfig<*>.() -> Unit = {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.d("[Ktor]") { message }
                }
            }
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(HttpCache)

        block()
    }
    return if (engine != null) HttpClient(engine, defaultBlock) else HttpClient(defaultBlock)
}