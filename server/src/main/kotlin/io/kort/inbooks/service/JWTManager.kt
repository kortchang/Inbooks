package io.kort.inbooks.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import io.kort.inbooks.data.model.User
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.auth.jwt.JWTCredential
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

interface JWTManager {
    fun realm(): String
    fun verifier(): JWTVerifier

    fun validate(credential: JWTCredential): Boolean
    fun create(userId: String): String
}

class KtorJWTManager(private val environment: ApplicationEnvironment) : JWTManager {

    private val algorithm get() = Algorithm.HMAC512(environment.config.property("jwt.secret").getString())
    private val audience get() = environment.config.property("jwt.audience").getString()
    private val issuer get() = environment.config.property("jwt.issuer").getString()

    override fun realm(): String {
        return environment.config.property("jwt.realm").getString()
    }

    override fun verifier(): JWTVerifier {
        return JWT
            .require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }

    override fun validate(credential: JWTCredential): Boolean {
        return if (credential.subject.isNullOrBlank()) false
        else {
            transaction {
                User.findById(UUID.fromString(credential.subject)) != null
            }
        }
    }

    override fun create(userId: String): String {
        return JWT.create()
            .withSubject(userId)
            .withIssuer(issuer)
            .withAudience(audience)
            .sign(algorithm)
    }
}