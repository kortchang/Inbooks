package io.kort.inbooks.plugin

import io.kort.inbooks.common.model.common.RemoteSerializerModel
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

val RemoteJson = Json {
    serializersModule = RemoteSerializerModel
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(RemoteJson)
    }
}