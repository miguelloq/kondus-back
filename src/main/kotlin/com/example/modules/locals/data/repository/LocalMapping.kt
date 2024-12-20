package com.example.modules.locals.data.repository

import com.example.core.data.repository.CoreUserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Locals : IntIdTable("locals") {
    val street = varchar("street", 255)
    val number = integer("number")
    val postal = char("postal", 8)
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val type = varchar("type", 255)
    val userId = integer("user_id").references(CoreUserTable.userId, onDelete = ReferenceOption.CASCADE)
}