package io.kort.inbooks.service

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServiceModule = module {
    singleOf(::BCryptPasswordEncryptor) bind PasswordEncryptor::class
    singleOf(::DefaultEmailVerificationCodeGenerator) bind EmailVerificationCodeGenerator::class
    singleOf(::ResendEmailSender) bind EmailSender::class
}