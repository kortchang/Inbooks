package io.kort.inbooks.service

import java.util.Locale

interface EmailVerificationCodeGenerator {
    fun generate(): String
}

class DefaultEmailVerificationCodeGenerator : EmailVerificationCodeGenerator {
    override fun generate(): String {
        return (100000..999999).random().toString().format("%06d", Locale.US)
    }
}