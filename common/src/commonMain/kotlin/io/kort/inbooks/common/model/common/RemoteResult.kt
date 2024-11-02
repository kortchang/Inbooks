package io.kort.inbooks.common.model.common

import arrow.core.raise.Raise
import arrow.core.raise.recover
import io.kort.inbooks.common.model.common.error.BusinessError
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface RemoteResult<out BE : BusinessError, out S> {
    val statusCode: Int

    @Serializable
    data class Success<out S>(val value: S) : RemoteResult<Nothing, S> {
        override val statusCode: Int get() = 200
    }

    @Serializable
    sealed interface Failure<out BE : BusinessError> : RemoteResult<BE, Nothing> {
        @Serializable
        data object AuthenticationFailure : Failure<Nothing> {
            override val statusCode: Int get() = 401
        }

        @Serializable
        data class ServerFailure(override val statusCode: Int) : Failure<Nothing>

        @Serializable
        data class BusinessFailure<out BE : BusinessError>(val error: BE) : Failure<BE> {
            override val statusCode: Int get() = 400
        }
    }
}

@JvmInline
value class RemoteResultRaise<BE : BusinessError>(private val raise: Raise<RemoteResult<BE, Nothing>>) :
    Raise<RemoteResult<BE, Nothing>> by raise {
    fun <S> RemoteResult<BE, S>.bind(): S = when (this) {
        is RemoteResult.Success -> value
        is RemoteResult.Failure -> raise.raise(this)
    }
}

@JvmInline
value class RemoteBusinessResultRaise<BE : BusinessError>(private val raise: Raise<BE>) : Raise<BE> by raise

inline fun <BE : BusinessError, S> runRemoteCatching(block: RemoteResultRaise<BE>.() -> S): RemoteResult<BE, S> =
    recover({ RemoteResult.Success(block(RemoteResultRaise(this))) }) { e: RemoteResult<BE, Nothing> -> e }

operator fun <BE : BusinessError> BE.unaryMinus() = RemoteResult.Failure.BusinessFailure(this)