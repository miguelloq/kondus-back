package com.example.modules.locals.data.repository

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


object Houses : IntIdTable("houses") {
    val localId = integer("local_id").references(Locals.id, onDelete = ReferenceOption.CASCADE)
    val description = varchar("description", 255)
}

class HouseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<HouseEntity>(Houses)

    var localId by Houses.localId
    var description by Houses.description
}