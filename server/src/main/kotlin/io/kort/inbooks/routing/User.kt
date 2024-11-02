package io.kort.inbooks.routing

import io.kort.inbooks.common.model.user.parameter.RemoteUserParameter.*
import io.kort.inbooks.data.repository.UserRepository
import io.kort.inbooks.service.JWTManager
import io.kort.inbooks.tool.respondEither
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.get

fun Application.user() {
    routing {
        route("/api/v1/users") {
            post {
                val parameter = call.receive<CreateParameter>()
                call.respondEither(this@user.get<UserRepository>().create(parameter.displayName, parameter.email, parameter.password))
            }

            post("/send-verify-email") {
                val parameter = call.receive<SendVerifyEmailParameter>()
                call.respondEither(this@user.get<UserRepository>().sendVerifyEmail(parameter.email))
            }

            post("/verify-email") {
                val parameter = call.receive<VerifyEmailParameter>()
                call.respondEither(this@user.get<UserRepository>().verifyEmail(parameter.email, parameter.code))
            }

            post("/login") {
                val parameter = call.receive<LoginParameter>()
                val either = this@user.get<UserRepository>().loginByEmail(parameter.email, parameter.password)
                either
                    .onLeft { call.respondEither(either) }
                    .onRight {
                        val userId = it.id
                        call.respond(HttpStatusCode.OK, this@user.get<JWTManager>().create(userId))
                    }
            }

            authenticate {
                get {
                    val userId = call.principal<JWTPrincipal>()?.subject
                    if (userId == null) {
                        call.respond(HttpStatusCode.Unauthorized)
                    } else {
                        call.respondEither(this@user.get<UserRepository>().get(userId))
                    }
                }
            }
        }
    }
}