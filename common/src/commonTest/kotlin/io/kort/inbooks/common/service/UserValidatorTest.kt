package io.kort.inbooks.common.service

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UserValidatorTest : FunSpec({
    test("displayNameIsValid should return true for non-blank display name") {
        UserValidator.displayNameIsValid("John Doe") shouldBe true
    }

    test("displayNameIsValid should return false for blank display name") {
        UserValidator.displayNameIsValid("") shouldBe false
    }

    test("emailIsValid should return true for valid email") {
        UserValidator.emailIsValid("test@example.com") shouldBe true
    }

    test("emailIsValid should return false for invalid email") {
        UserValidator.emailIsValid("invalid-email") shouldBe false
    }

    test("passwordIsValid should return true for valid password") {
        UserValidator.passwordIsValid("Valid123!") shouldBe true
    }

    test("passwordIsValid should return false for password with less than 8 characters") {
        UserValidator.passwordIsValid("Short1!") shouldBe false
    }
})