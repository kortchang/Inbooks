package io.kort.inbooks.plugin

import io.kort.inbooks.common.model.common.RemoteResult
import io.kort.inbooks.service.JWTManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import org.koin.ktor.ext.get

fun Application.configureAuthentication() {
    val jwtManager: JWTManager = get()
    authentication {
        jwt {
            realm = jwtManager.realm()
            verifier(jwtManager.verifier())

            validate { credential ->
                if (jwtManager.validate(credential)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}