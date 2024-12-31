package io.kort.inbooks.common.model.user.error

import io.kort.inbooks.common.model.common.error.BusinessError
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface RemoteUserError : BusinessError {
    sealed interface CreateError : RemoteUserError {
        @Serializable
        @SerialName("User.Create.AlreadyExists")
        data object AlreadyExists : CreateError

        @Serializable
        @SerialName("User.Create.InvalidEmail")
        data object InvalidEmail : CreateError

        @Serializable
        @SerialName("User.Create.PasswordHasInvalidCharacter")
        data object PasswordHasInvalidCharacter : CreateError

        @Serializable
        @SerialName("User.Create.PasswordTooShort")
        data object PasswordTooShort : CreateError

        @Serializable
        @SerialName("User.Create.InvalidDisplayName")
        data object InvalidDisplayName : CreateError
    }

    @Serializable
    sealed interface RequireVerifyEmailError : RemoteUserError {
        @Serializable
        @SerialName("User.RequireVerifyEmail.EmailNotFound")
        data object EmailNotFound : RequireVerifyEmailError

        @Serializable
        @SerialName("User.RequireVerifyEmail.EmailAlreadyVerified")
        data object EmailAlreadyVerified : RequireVerifyEmailError
    }

    @Serializable
    sealed interface VerifyEmailError : RemoteUserError {
        @Serializable
        @SerialName("User.VerifyEmail.UserNotFound")
        data object UserNotFound : VerifyEmailError

        @Serializable
        @SerialName("User.VerifyEmail.VerifyEmailNotSentYet")
        data object VerifyEmailNotSentYet : VerifyEmailError

        @Serializable
        @SerialName("User.VerifyEmail.InvalidToken")
        data object InvalidToken : VerifyEmailError

        @Serializable
        @SerialName("User.VerifyEmail.TokenExpired")
        data object TokenExpired : VerifyEmailError

        @Serializable
        @SerialName("User.VerifyEmail.AlreadyVerified")
        data object AlreadyVerified : VerifyEmailError
    }

    sealed interface LoginError : RemoteUserError {
        @Serializable
        @SerialName("User.Login.NotExistEmailOrWrongPassword")
        data object NotExistEmailOrWrongPassword : LoginError
    }

    @Serializable
    sealed interface GetError : RemoteUserError {
        @Serializable
        @SerialName("User.Get.UserNotFound")
        data object UserNotFound : GetError
    }
}