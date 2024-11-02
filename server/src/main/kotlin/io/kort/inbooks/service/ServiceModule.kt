package io.kort.inbooks.service

import io.kort.inbooks.common.service.UserValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServiceModule = module {
    singleOf(::BCryptPasswordEncryptor) bind PasswordEncryptor::class
    singleOf(::DefaultEmailVerificationCodeGenerator) bind EmailVerificationCodeGenerator::class
    singleOf(::ResendEmailSender) bind EmailSender::class
    singleOf(::KtorJWTManager) bind JWTManager::class
    single { UserValidator }
}