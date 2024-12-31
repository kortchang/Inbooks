package io.kort.inbooks.common.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import kotlin.text.all
import kotlin.text.contains
import kotlin.text.toRegex

object UserValidator {
    fun displayNameIsValid(displayName: String): Boolean {
        return displayName.isNotBlank()
    }

    fun emailIsValid(email: String): Boolean {
        val isValid = """^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$"""
            .toRegex()
            .matches(email)

        return isValid
    }

    fun passwordIsValid(password: String): Either<PasswordError, Unit> = either {
        val isLetterOrDigit = """[a-zA-Z0-9]""".toRegex()
        val validSpecialCharacters = "!@#$%^&*()_+{}|:<>?~`-=[]\\;',./"
        val characterValid = password.all {
            isLetterOrDigit.matches(it.toString()) || it in validSpecialCharacters
        }
        val lengthValid = password.length >= 8
        ensure(lengthValid) { PasswordError.PasswordTooShort }
        ensure(characterValid) { PasswordError.PasswordHasInvalidCharacter }
    }

    enum class PasswordError {
        PasswordTooShort,
        PasswordHasInvalidCharacter,
    }
}