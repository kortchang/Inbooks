package io.kort.inbooks.data.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object Users : UUIDTable() {
    val displayName = text("display_name")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val modifiedAt = timestamp("modified_at").nullable()
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    var displayName by Users.displayName
    var createdAt by Users.createdAt
    var modifiedAt by Users.modifiedAt

    companion object : UUIDEntityClass<User>(Users)
}