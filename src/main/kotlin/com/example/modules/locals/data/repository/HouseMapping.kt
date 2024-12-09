package com.example.modules.locals.data.repository

import com.example.core.data.repository.CoreUserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Houses : IntIdTable("houses") {
    val localId = integer("local_id").references(Locals.id, onDelete = ReferenceOption.CASCADE)
    val description = varchar("description", 255)
}

object UsersHouses : Table("users_houses") {
    val userId = integer("user_id").references(CoreUserTable.userId, onDelete = ReferenceOption.CASCADE)
    val houseId = integer("house_id").references(Houses.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, houseId)
}