package io.kort.inbooks.tool

import arrow.core.Either
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.reflect.typeInfo

suspend inline fun <reified A, reified B> ApplicationCall.respondEither(value: Either<A, B>) {
    value.fold(
        { respond(HttpStatusCode(430, "Business Error"), it, typeInfo<A>()) },
        { respond(HttpStatusCode.OK, it, typeInfo<B>()) }
    )
}