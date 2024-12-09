package com.example.core.data.repository

import org.jetbrains.exposed.sql.Table

object CoreUserTable : Table("users") {
    val userId = integer("id").autoIncrement()

    override val primaryKey = PrimaryKey(userId)
}