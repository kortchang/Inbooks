package service

import arrow.core.Either
import arrow.core.serialization.EitherSerializer
import kotlinx.serialization.modules.SerializersModule

val RemoteSerializerModel = SerializersModule {
    contextual(Either::class) { (left, right) ->
        EitherSerializer(left, right)
    }
}