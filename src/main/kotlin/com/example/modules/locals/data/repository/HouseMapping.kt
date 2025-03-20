package com.example.modules.locals.data.repository

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


object Houses : IntIdTable("houses") {
    val localId = integer("local_id").references(Locals.id, onDelete = ReferenceOption.CASCADE)
    val description = varchar("description", 255)
}