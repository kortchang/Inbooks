package service

import arrow.core.Either
import kotlinx.rpc.RPC

interface RemoteUserRepository : RPC {
    suspend fun create(displayName: String, email: String, password: String): Either<CreateUserError, Unit>
    suspend fun requireVerifyEmail(email: String): Either<RequireVerifyEmailError, Unit>
    suspend fun verifyEmail(email: String, token: String): Either<VerifyEmailError, Unit>
    suspend fun login(email: String, password: String): Either<LoginError, String>
}

sealed class CreateUserError(message: String) : Error(message) {
    data object UserAlreadyExists : CreateUserError("User already exists")
    data object InvalidEmail : CreateUserError("Invalid email")
    data object InvalidPassword : CreateUserError("Invalid password")
    data object InvalidDisplayName : CreateUserError("Invalid display name")
}

sealed class RequireVerifyEmailError(message: String) : Error(message) {
    data object EmailNotFound : RequireVerifyEmailError("User not found")
    data object EmailAlreadyVerified : RequireVerifyEmailError("Email already verified")
}

sealed class VerifyEmailError(message: String) : Error(message) {
    data object UserNotFound : VerifyEmailError("User not found")
    data object HasNotBeenRequired : VerifyEmailError("Email has not been required to verify")
    data object InvalidToken : VerifyEmailError("Invalid token")
    data object TokenExpired : VerifyEmailError("Token expired")
}

sealed class LoginError(message: String) : Error(message) {
    data object UserNotFound : LoginError("User not found")
    data object InvalidPassword : LoginError("Invalid password")
}