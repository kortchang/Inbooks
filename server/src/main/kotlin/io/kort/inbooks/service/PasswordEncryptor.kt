package io.kort.inbooks.service

import org.springframework.security.crypto.bcrypt.BCrypt

interface PasswordEncryptor {
    fun encrypt(password: String): String
    fun verify(givenPassword: String, passwordHash: String): Boolean
}

class BCryptPasswordEncryptor : PasswordEncryptor {
    override fun encrypt(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    override fun verify(givenPassword: String, passwordHash: String): Boolean {
        return BCrypt.checkpw(givenPassword, passwordHash)
    }
}