package com.example.modules.locals.data.repository

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.IntEntityClass

object Locals : IntIdTable("locals") {
    val street = varchar("street", 255)
    val number = integer("number")
    val postal = char("postal", 8)
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val type = varchar("type", 255)
}

class LocalEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LocalEntity>(Locals)

    var street by Locals.street
    var number by Locals.number
    var postal by Locals.postal
    var name by Locals.name
    var description by Locals.description
    var type by Locals.type
}
