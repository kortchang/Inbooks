package io.kort.inbooks

import UserValidator
import arrow.core.raise.either
import io.kort.inbooks.data.DatabaseConfiguration
import io.kort.inbooks.data.migration.FlywayMigrationManager
import io.kort.inbooks.data.repository.DefaultRemoteUserRepository
import io.kort.inbooks.service.EmailSender
import io.kort.inbooks.service.EmailVerificationCodeGenerator
import io.kort.inbooks.service.PasswordEncryptor
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import service.CreateUserError
import service.VerifyEmailError
import kotlin.time.Duration.Companion.minutes


class UserTest : FunSpec({
    val coroutineContext = UnconfinedTestDispatcher()
    val validator = UserValidator
    val passwordEncryptor = object : PasswordEncryptor {
        override fun encrypt(password: String): String = password

        override fun verify(givenPassword: String, passwordHash: String): Boolean = givenPassword == passwordHash
    }

    val emailSender = mockk<EmailSender> {
        every { sendNoReply(any(), any(), any()) } answers {
            println(
                "to: ${firstArg<String>()}, subject: ${secondArg<String>()}, body: ${thirdArg<String>()}"
            )
            either { Unit }
        }
    }

    val emailVerificationCodeGenerator = object : EmailVerificationCodeGenerator {
        override fun generate(): String = "123456"
    }

    val userRepository = DefaultRemoteUserRepository(
        coroutineContext = coroutineContext,
        validator = validator,
        passwordEncryptor = passwordEncryptor,
        emailVerificationCodeGenerator = emailVerificationCodeGenerator,
        emailSender = emailSender
    )

    val databaseConfiguration = object : DatabaseConfiguration {
        override val url = System.getenv("DATABASE_URL")
        override val user = System.getenv("DATABASE_USER")
        override val password = System.getenv("DATABASE_PASSWORD")
    }
    val database = Database.connect(
        url = databaseConfiguration.url,
        user = databaseConfiguration.user,
        password = databaseConfiguration.password
    )
    val migrationManager = FlywayMigrationManager(databaseConfiguration)
    TransactionManager.defaultDatabase = database

    transaction {
        SchemaUtils.listTables().forEach {
            println("Dropping table $it")
            exec("DROP TABLE $it")
        }
    }
    migrationManager.migrate()

    context("create user") {
        testOrder = TestCaseOrder.Sequential

        test("fail to create user with invalid display name") {
            userRepository.create(
                displayName = "",
                email = "",
                password = ""
            ).shouldBeLeft(CreateUserError.InvalidDisplayName)
        }

        test("failed to create user with invalid email") {
            userRepository.create(
                displayName = "test",
                email = "test",
                password = ""
            ).shouldBeLeft(CreateUserError.InvalidEmail)
        }

        test("failed to create user with short password") {
            userRepository.create(
                displayName = "test",
                email = "test@test.test",
                password = "test"
            ).shouldBeLeft(CreateUserError.InvalidPassword)
        }

        test("failed to create user with invalid password") {
            userRepository.create(
                displayName = "test",
                email = "test@test.test",
                password = "督督督督督督督督"
            ).shouldBeLeft(CreateUserError.InvalidPassword)
        }

        test("success to create user") {
            userRepository
                .create(displayName = "test", email = "test@test.test", password = "testtest")
                .shouldBeRight(Unit)

            verify { emailSender.sendNoReply(to = "test@test.test", subject = any(), text = any()) }
        }

        test("failed to create user with same email") {
            userRepository
                .create(displayName = "test", email = "test@test.test", password = "testtest")
                .shouldBeLeft(CreateUserError.UserAlreadyExists)
        }
    }

    context("verify email") {
        test("failed to verify email with invalid email") {
            userRepository
                .verifyEmail(email = "test", token = "123")
                .shouldBeLeft(VerifyEmailError.UserNotFound)
        }

        test("failed to verify email with invalid token") {
            userRepository
                .verifyEmail(email = "test@test.test", token = "111111")
                .shouldBeLeft(VerifyEmailError.InvalidToken)
        }

        test("failed to verify email with expired token") {
            mockkObject(Clock.System)
            every { Clock.System.now() } returns Instant.fromEpochMilliseconds(System.currentTimeMillis()) + 10.minutes

            userRepository
                .verifyEmail(email = "test@test.test", token = "123456")
                .shouldBeLeft(VerifyEmailError.TokenExpired)

            unmockkObject(Clock.System)
        }

        test("success to verify email") {
            userRepository
                .verifyEmail(email = "test@test.test", token = "123456")
                .shouldBeRight()
        }

        test("failed to verify email that already verified") {
            userRepository
                .verifyEmail(email = "test@test.test", token = "123456")
                .shouldBeLeft(VerifyEmailError.HasNotBeenRequired)
        }
    }

    coroutineContext.cancel()
})