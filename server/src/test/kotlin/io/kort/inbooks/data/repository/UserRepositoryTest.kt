package io.kort.inbooks.data.repository

import arrow.core.Either
import arrow.core.right
import io.kort.inbooks.common.service.UserValidator
import io.kort.inbooks.common.model.user.RemoteUser
import io.kort.inbooks.common.model.user.error.RemoteUserError.*
import io.kort.inbooks.data.DatabaseConfiguration
import io.kort.inbooks.data.migration.FlywayMigrationManager
import io.kort.inbooks.service.EmailSender
import io.kort.inbooks.service.EmailVerificationCodeGenerator
import io.kort.inbooks.service.PasswordEncryptor
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration.Companion.minutes

class UserRepositoryTest : FunSpec({
    lateinit var emailSender: EmailSender
    lateinit var userRepository: UserRepository

    beforeSpec {
        coroutineTestScope = true

        val validator = UserValidator
        val passwordEncryptor = object : PasswordEncryptor {
            override fun encrypt(password: String): String = password

            override fun verify(givenPassword: String, passwordHash: String): Boolean {
                return givenPassword == passwordHash
            }
        }

        emailSender = mockk<EmailSender> {
            every { sendNoReply(any(), any(), any()) } answers {
                Unit.right()
            }
        }

        val emailVerificationCodeGenerator = object : EmailVerificationCodeGenerator {
            override fun generate(): String = "123456"
        }

        userRepository = UserRepository(
            validator = validator,
            passwordEncryptor = passwordEncryptor,
            emailVerificationCodeGenerator = emailVerificationCodeGenerator,
            emailSender = emailSender
        )

        val databaseConfiguration = object : DatabaseConfiguration {
            override val url = "jdbc:postgresql://localhost:5433/test"
            override val user = "postgres"
            override val password = "password"
        }
        val database = Database.connect(
            url = databaseConfiguration.url,
            user = databaseConfiguration.user,
            password = databaseConfiguration.password
        )
        TransactionManager.defaultDatabase = database

        transaction {
            SchemaUtils.dropSchema(Schema("public"))
            SchemaUtils.listTables().forEach {
                exec("DROP TABLE $it")
            }
        }
        FlywayMigrationManager(databaseConfiguration).migrate()
    }

    context("create user") {
        testOrder = TestCaseOrder.Sequential

        test("fail to create user with invalid display name") {
            userRepository.create(
                displayName = "",
                email = "",
                password = ""
            ).shouldBeLeft(CreateError.InvalidDisplayName)
        }

        test("failed to create user with invalid email") {
            userRepository.create(
                displayName = "test",
                email = "test",
                password = ""
            ).shouldBeLeft(CreateError.InvalidEmail)
        }

        test("failed to create user with short password") {
            userRepository.create(
                displayName = "test",
                email = "test@test.test",
                password = "test"
            ).shouldBeLeft(CreateError.InvalidPassword)
        }

        test("failed to create user with invalid password") {
            userRepository.create(
                displayName = "test",
                email = "test@test.test",
                password = "督督督督督督督督"
            ).shouldBeLeft(CreateError.InvalidPassword)
        }

        test("success to create user") {
            userRepository
                .create(displayName = "test", email = "test@test.test", password = "testtest")
                .shouldBeRight(Unit)
        }

        test("failed to create user with same email") {
            userRepository
                .create(displayName = "test", email = "test@test.test", password = "testtest")
                .shouldBeLeft(CreateError.AlreadyExists)
        }
    }

    context("require verify email") {
        test("failed to require verify email with invalid email") {
            userRepository
                .sendVerifyEmail(email = "test")
                .shouldBeLeft(RequireVerifyEmailError.EmailNotFound)
        }

        test("failed to verify email that not sent yet") {
            userRepository
                .verifyEmail(email = "test@test.test", code = "123456")
                .shouldBeLeft(VerifyEmailError.VerifyEmailNotSentYet)
        }

        test("success to require verify email") {
            userRepository
                .sendVerifyEmail(email = "test@test.test")
                .shouldBeRight(Unit)

            verify { emailSender.sendNoReply(to = "test@test.test", subject = any(), text = any()) }
        }

        context("verify email") {
            test("failed to verify email with invalid email") {
                userRepository
                    .verifyEmail(email = "test", code = "123")
                    .shouldBeLeft(VerifyEmailError.UserNotFound)
            }

            test("failed to verify email with invalid token") {
                userRepository
                    .verifyEmail(email = "test@test.test", code = "111111")
                    .shouldBeLeft(VerifyEmailError.InvalidToken)
            }

            test("failed to verify email with expired token") {
                mockkObject(Clock.System)
                every { Clock.System.now() } returns Instant.fromEpochMilliseconds(System.currentTimeMillis()) + 10.minutes

                userRepository
                    .verifyEmail(email = "test@test.test", code = "123456")
                    .shouldBeLeft(VerifyEmailError.TokenExpired)

                unmockkObject(Clock.System)
            }

            test("success to verify email") {
                userRepository
                    .verifyEmail(email = "test@test.test", code = "123456")
                    .shouldBeRight(Unit)
            }

            test("failed to verify email that already verified") {
                userRepository
                    .verifyEmail(email = "test@test.test", code = "123456")
                    .shouldBeLeft(VerifyEmailError.AlreadyVerified)
            }

            test("failed to require verify email that already verified") {
                userRepository
                    .sendVerifyEmail(email = "test@test.test")
                    .shouldBeLeft(RequireVerifyEmailError.EmailAlreadyVerified)
            }
        }

        context("login") {
            test("failed to login with invalid email") {
                userRepository
                    .loginByEmail(email = "test", password = "test")
                    .shouldBeLeft(LoginError.NotExistEmailOrWrongPassword)
            }

            test("failed to login with invalid password") {
                userRepository
                    .loginByEmail(email = "test@test.test", password = "test")
                    .shouldBeLeft(LoginError.NotExistEmailOrWrongPassword)
            }

            test("success to login") {
                userRepository
                    .loginByEmail(email = "test@test.test", password = "testtest")
                    .shouldBeInstanceOf<Either.Right<RemoteUser>>()
                    .value
                    .should {
                        it.displayName shouldBe "test"
                        it.email shouldBe "test@test.test"
                    }
            }
        }
    }
})
