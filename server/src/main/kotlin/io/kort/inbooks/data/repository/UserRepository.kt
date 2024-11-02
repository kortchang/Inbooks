package io.kort.inbooks.data.repository

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import io.kort.inbooks.common.model.user.RemoteUser
import io.kort.inbooks.common.model.user.error.RemoteUserError.CreateError
import io.kort.inbooks.common.model.user.error.RemoteUserError.GetError
import io.kort.inbooks.common.model.user.error.RemoteUserError.LoginError
import io.kort.inbooks.common.model.user.error.RemoteUserError.RequireVerifyEmailError
import io.kort.inbooks.common.model.user.error.RemoteUserError.VerifyEmailError
import io.kort.inbooks.common.service.UserValidator
import io.kort.inbooks.data.model.Authentication
import io.kort.inbooks.data.model.Authentications
import io.kort.inbooks.data.model.User
import io.kort.inbooks.service.EmailSender
import io.kort.inbooks.service.EmailVerificationCodeGenerator
import io.kort.inbooks.service.PasswordEncryptor
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

class UserRepository(
    private val validator: UserValidator,
    private val passwordEncryptor: PasswordEncryptor,
    private val emailVerificationCodeGenerator: EmailVerificationCodeGenerator,
    private val emailSender: EmailSender,
) {
    suspend fun create(
        displayName: String,
        email: String,
        password: String
    ): Either<CreateError, Unit> {
        return either {
            ensure(validator.displayNameIsValid(displayName)) { CreateError.InvalidDisplayName }
            ensure(validator.emailIsValid(email)) { CreateError.InvalidEmail }
            ensure(validator.passwordIsValid(password)) { CreateError.InvalidPassword }

            newSuspendedTransaction(Dispatchers.IO) {
                val userExist = Authentication.find { Authentications.email eq email }.firstOrNull() != null
                ensure(userExist.not()) { CreateError.AlreadyExists }

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
        }
    }

    suspend fun sendVerifyEmail(email: String): Either<RequireVerifyEmailError, Unit> {
        return either {
            newSuspendedTransaction(Dispatchers.IO) {
                val authentication = Authentication.find { Authentications.email eq email }.firstOrNull()
                ensure(authentication != null) { RequireVerifyEmailError.EmailNotFound }
                ensure(authentication.emailVerified.not()) { RequireVerifyEmailError.EmailAlreadyVerified }

                val verificationCode = emailVerificationCodeGenerator.generate()

                authentication.emailVerificationCode = verificationCode
                authentication.emailVerificationExpiredAt = Clock.System.now().plus(10.minutes)

                emailSender.sendNoReply(
                    to = email,
                    subject = "Confirm your email address",
                    text = "Your verification code for Inbooks is $verificationCode"
                )
            }
        }
    }

    suspend fun verifyEmail(
        email: String,
        code: String
    ): Either<VerifyEmailError, Unit> {
        return either {
            newSuspendedTransaction(Dispatchers.IO) {
                val authentication = Authentication.find { Authentications.email eq email }.firstOrNull()
                ensureNotNull(authentication) { VerifyEmailError.UserNotFound }
                ensure(authentication.emailVerified.not()) { VerifyEmailError.AlreadyVerified }
                ensure(authentication.emailVerificationCode != null && authentication.emailVerificationExpiredAt != null) {
                    VerifyEmailError.VerifyEmailNotSentYet
                }
                ensure(authentication.emailVerificationCode == code) { VerifyEmailError.InvalidToken }
                ensure(authentication.emailVerificationExpiredAt?.let { it > Clock.System.now() } == true) {
                    VerifyEmailError.TokenExpired
                }

                authentication.emailVerified = true
                authentication.emailVerificationCode = null
                authentication.emailVerificationExpiredAt = null
            }
        }
    }

    suspend fun loginByEmail(email: String, password: String): Either<LoginError, RemoteUser> {
        return either {
            newSuspendedTransaction(Dispatchers.IO) {
                val authentication = Authentication.find { Authentications.email eq email }.firstOrNull()
                ensureNotNull(authentication) { LoginError.NotExistEmailOrWrongPassword }
                val passwordHash = authentication.passwordHash
                ensureNotNull(passwordHash) { LoginError.NotExistEmailOrWrongPassword }
                ensure(passwordEncryptor.verify(password, passwordHash)) { LoginError.NotExistEmailOrWrongPassword }
                authentication.toRemoteUser()
            }
        }
    }

    suspend fun get(id: String): Either<GetError, RemoteUser> {
        return either {
            newSuspendedTransaction(Dispatchers.IO) {
                /**
                 * 假設未來有 SSO 登入：
                 *
                 * 1. 先檢查是否有用密碼登入的 auth，選擇用他的 Email（因為 SSO 的 email 資訊我改不到）
                 * 且如果他都只有用 SSO 登入，那不能改 Email。除非他走忘記密碼，創一個用密碼驗證的 Auth 才可以改
                 * 2. 隨便用 SSO 的提供的 Email
                 */
                val authentication = Authentication.find {
                    (Authentications.userId eq UUID.fromString(id))
                }.run {
                    firstOrNull { it.passwordHash != null } ?: firstOrNull()
                }

                ensureNotNull(authentication) { GetError.UserNotFound }
                authentication.toRemoteUser()
            }
        }
    }

    private fun Authentication.toRemoteUser(): RemoteUser {
        return RemoteUser(
            id = user.id.value.toString(),
            displayName = user.displayName,
            email = email,
            emailVerified = emailVerified,
            createdAt = user.createdAt,
            modifiedAt = user.modifiedAt
        )
    }
}
