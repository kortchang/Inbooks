package io.kort.inbooks.data.repository

import UserValidator
import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import io.kort.inbooks.data.model.Authentication
import io.kort.inbooks.data.model.Authentications
import io.kort.inbooks.data.model.User
import io.kort.inbooks.service.EmailSender
import io.kort.inbooks.service.EmailVerificationCodeGenerator
import io.kort.inbooks.service.PasswordEncryptor
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import service.CreateUserError
import service.RemoteUserRepository
import service.RequireVerifyEmailError
import service.VerifyEmailError
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.minutes

class DefaultRemoteUserRepository(
    override val coroutineContext: CoroutineContext,
    private val validator: UserValidator,
    private val passwordEncryptor: PasswordEncryptor,
    private val emailVerificationCodeGenerator: EmailVerificationCodeGenerator,
    private val emailSender: EmailSender,
) : RemoteUserRepository {
    override suspend fun create(
        displayName: String,
        email: String,
        password: String
    ): Either<CreateUserError, Unit> {
        return either {
            ensure(validator.displayNameIsValid(displayName)) { CreateUserError.InvalidDisplayName }
            ensure(validator.emailIsValid(email)) { CreateUserError.InvalidEmail }
            ensure(validator.passwordIsValid(password)) { CreateUserError.InvalidPassword }

            newSuspendedTransaction(Dispatchers.IO) {
                val userExist = Authentication.find { Authentications.email eq email }.firstOrNull() != null
                ensure(userExist.not()) { CreateUserError.UserAlreadyExists }

                val user = User.new {
                    this.displayName = displayName
                }

                Authentication.new {
                    this.user = user
                    this.email = email
                    this.emailVerified = false
                    this.passwordHash = passwordEncryptor.encrypt(password)
                }
            }
            requireVerifyEmail(email)
        }
    }

    override suspend fun requireVerifyEmail(email: String): Either<RequireVerifyEmailError, Unit> {
        return either {
            newSuspendedTransaction(Dispatchers.IO) {
                val authentication = Authentication.find { Authentications.email eq email }.firstOrNull()
                ensure(authentication != null) { RequireVerifyEmailError.EmailNotFound }
                ensure(authentication.emailVerified.not()) { RequireVerifyEmailError.EmailAlreadyVerified }

                val verificationCode = emailVerificationCodeGenerator.generate()

                authentication.emailVerificationCode = verificationCode
                authentication.emailVerificationExpiredAt = Clock.System.now().plus(10.minutes)

                sendVerifyEmail(email, verificationCode)
            }
        }
    }

    private fun sendVerifyEmail(email: String, token: String) {
        emailSender.sendNoReply(
            to = email,
            subject = "Confirm your email address",
            text = "Your verification code for Inbooks is $token"
        )
    }

    override suspend fun verifyEmail(
        email: String,
        token: String
    ): Either<VerifyEmailError, Unit> {
        return either {
            newSuspendedTransaction(Dispatchers.IO) {
                val authentication = Authentication.find { Authentications.email eq email }.firstOrNull()
                ensureNotNull(authentication) { VerifyEmailError.UserNotFound }
                ensure(
                    authentication.emailVerified.not() &&
                            authentication.emailVerificationCode != null &&
                            authentication.emailVerificationExpiredAt != null
                ) {
                    VerifyEmailError.HasNotBeenRequired
                }
                ensure(authentication.emailVerificationCode == token) { VerifyEmailError.InvalidToken }
                ensure(authentication.emailVerificationExpiredAt?.let { it > Clock.System.now() } == true) {
                    VerifyEmailError.TokenExpired
                }

                authentication.emailVerified = true
                authentication.emailVerificationCode = null
                authentication.emailVerificationExpiredAt = null
            }
        }
    }
}
