package io.kort.inbooks.common.model.common.error

import kotlinx.serialization.Serializable

interface RemoteError

interface BusinessError : RemoteError

@Serializable
sealed interface CommonError : RemoteError {
    @Serializable
    data object Unauthorized : CommonError

    @Serializable
    data class ServerError(val statusCode: Int) : CommonError
}
