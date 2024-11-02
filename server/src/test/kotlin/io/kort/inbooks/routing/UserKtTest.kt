package io.kort.inbooks.routing

import arrow.core.Either
import arrow.core.right
import io.kort.inbooks.common.model.user.RemoteUser
import io.kort.inbooks.common.model.user.parameter.RemoteUserParameter.CreateParameter
import io.kort.inbooks.common.model.user.parameter.RemoteUserParameter.LoginParameter
import io.kort.inbooks.common.model.user.parameter.RemoteUserParameter.SendVerifyEmailParameter
import io.kort.inbooks.common.model.user.parameter.RemoteUserParameter.VerifyEmailParameter
import io.kort.inbooks.service.EmailSender
import io.kort.inbooks.service.EmailVerificationCodeGenerator
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.TestApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

class UserKtTest : FunSpec({
    lateinit var testApplication: TestApplication
    lateinit var client: HttpClient
    beforeSpec {
        testApplication = TestApplication {
            environment {
                config = ApplicationConfig("application.conf")
            }
            application {
                loadKoinModules(
                    listOf(
                        module {
                            singleOf(::FixedEmailVerificationCodeGenerator) bind EmailVerificationCodeGenerator::class
                            singleOf(::MockEmailSender) bind EmailSender::class
                        },
                    )
                )
            }
        }
        client = testApplication.createClient {
            install(ContentNegotiation) { json() }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
        testApplication.start()
    }

    afterSpec {
        testApplication.stop()
    }

    test("create user") {
        client.post("/api/v1/users") {
            setBody(CreateParameter("test", "test@test.test", "testtest"))
        }.shouldHaveStatus(200)
    }

    test("send verify email") {
        client.post("/api/v1/users/send-verify-email") {
            setBody(SendVerifyEmailParameter("test@test.test"))
        }.shouldHaveStatus(200)
    }

    test("verify email") {
        client.post("/api/v1/users/verify-email") {
            setBody(VerifyEmailParameter("test@test.test", "123456"))
        }.shouldHaveStatus(200)
    }

    test("login") {
        var jwt: String
        client.post("/api/v1/users/login") {
            setBody(LoginParameter("test@test.test", "testtest"))
        }.apply { jwt = bodyAsText() }.shouldHaveStatus(200)

        client.get("api/v1/users") {
            bearerAuth(jwt)
        }.let {
            it.status shouldBe HttpStatusCode.OK
            it.body<RemoteUser>().should {
                it.displayName shouldBe "test"
                it.email shouldBe "test@test.test"
                it.emailVerified shouldBe true
            }
        }
    }
})

class FixedEmailVerificationCodeGenerator : EmailVerificationCodeGenerator {
    override fun generate(): String {
        return "123456"
    }
}

class MockEmailSender : EmailSender {
    override fun sendNoReply(to: String, subject: String, text: String): Either<Error, Unit> {
        return Unit.right()
    }
}