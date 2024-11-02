package io.kort.inbooks.service

import arrow.core.Either
import arrow.core.raise.either
import com.resend.Resend
import com.resend.core.exception.ResendException
import com.resend.services.emails.model.CreateEmailOptions
import io.ktor.server.application.ApplicationEnvironment

interface EmailSender {
    fun sendNoReply(
        to: String,
        subject: String,
        text: String
    ): Either<Error, Unit>
}

class ResendEmailSender(environment: ApplicationEnvironment) : EmailSender {
    private val resend = Resend(environment.config.property("resend.apiKey").getString())
    override fun sendNoReply(
        to: String,
        subject: String,
        text: String
    ): Either<Error, Unit> {
        val options = CreateEmailOptions.builder()
            .from("Inbooks <no-reply@auto.inbooks.tw>")
            .to(to)
            .subject(subject)
            .text(text)
            .build()

        return either<ResendException, Unit> {
            resend.emails().send(options)
        }.mapLeft<Error> { Error(it.message, it.cause) }
    }
}