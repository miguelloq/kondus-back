package com.example.modules.users.data.repository

import com.example.modules.locals.data.repository.Houses
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserTable: IntIdTable("users"){
    val name = varchar("name",20)
    val password = varchar("password",20)
    val email = varchar("email",40)
    val houseId = integer("house_id").references(Houses.id, onDelete = ReferenceOption.CASCADE)
}

class UserEntity(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<UserEntity>(UserTable)

    var name by UserTable.name
    var password by UserTable.password
    var email by UserTable.email
    var houseId by UserTable.houseId
}