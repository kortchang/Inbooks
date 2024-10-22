package io.kort.inbooks.data.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object Authentications : UUIDTable() {
    val userId = reference("user_id", Users)
    val email = varchar("email", 256)
    val emailVerified = bool("email_verified").default(false)
    val emailVerificationCode = varchar("email_verification_code", 10).nullable()
    val emailVerificationExpiredAt = timestamp("email_verification_expired_at").nullable()
    val passwordHash = varchar("password_hash", 256).nullable()
}

class Authentication(id: EntityID<UUID>) : UUIDEntity(id) {
    var user by User referencedOn Authentications.userId
    var email by Authentications.email
    var emailVerified by Authentications.emailVerified
    var emailVerificationCode by Authentications.emailVerificationCode
    var emailVerificationExpiredAt by Authentications.emailVerificationExpiredAt
    var passwordHash by Authentications.passwordHash

    companion object : UUIDEntityClass<Authentication>(Authentications)
}