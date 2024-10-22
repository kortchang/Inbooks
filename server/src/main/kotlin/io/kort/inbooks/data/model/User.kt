package io.kort.inbooks.data.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object Users : UUIDTable() {
    val displayName = varchar("display_name", 50)
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    var displayName by Users.displayName

    companion object : UUIDEntityClass<User>(Users)
}