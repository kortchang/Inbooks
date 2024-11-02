package io.kort.inbooks.common.model.user.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface RemoteUserParameter {
    @Serializable
    data class CreateParameter(
        @SerialName("display_name") val displayName: String,
        @SerialName("email") val email: String,
        @SerialName("password") val password: String,
    ) : RemoteUserParameter

    @Serializable
    data class SendVerifyEmailParameter(
        @SerialName("email") val email: String,
    ) : RemoteUserParameter

    @Serializable
    data class VerifyEmailParameter(
        @SerialName("email") val email: String,
        @SerialName("code") val code: String,
    ) : RemoteUserParameter

    @Serializable
    data class LoginParameter(
        @SerialName("email") val email: String,
        @SerialName("password") val password: String,
    ) : RemoteUserParameter
}