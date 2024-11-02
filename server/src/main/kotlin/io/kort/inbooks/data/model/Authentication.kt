package io.kort.inbooks.data.model

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object Authentications : UUIDTable() {
    val userId = reference("user_id", Users)
    val email = text("email")
    val emailVerified = bool("email_verified").default(false)
    val emailVerificationCode = text("email_verification_code").nullable()
    val emailVerificationExpiredAt = timestamp("email_verification_expired_at").nullable()
    val passwordHash = text("password_hash").nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val modifiedAt = timestamp("modified_at").nullable()
}

class Authentication(id: EntityID<UUID>) : UUIDEntity(id) {
    var user by User referencedOn Authentications.userId
    var email by Authentications.email
    var emailVerified by Authentications.emailVerified
    var emailVerificationCode by Authentications.emailVerificationCode
    var emailVerificationExpiredAt by Authentications.emailVerificationExpiredAt
    var passwordHash by Authentications.passwordHash
    var createdAt by Authentications.createdAt
    var modifiedAt by Authentications.modifiedAt

    companion object : UUIDEntityClass<Authentication>(Authentications)
}