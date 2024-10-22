package io.kort.inbooks.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import kotlinx.rpc.krpc.ktor.server.RPC
import kotlinx.rpc.krpc.ktor.server.rpc
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.get
import service.RemoteSerializerModel
import service.RemoteUserRepository

fun Application.configureRPC() {
    install(RPC)

    routing {
        rpc("/api/v1") {
            rpcConfig {
                serialization {
                    json {
                        serializersModule = RemoteSerializerModel
                    }
                }
            }

            registerService { coroutineContext -> get<RemoteUserRepository> { parametersOf(coroutineContext) } }
        }
    }
}