package com.example.modules.items.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Categories : IntIdTable("categories") {
    val name = varchar("name", 100).uniqueIndex()
}

object ItemCategories : Table("item_categories") {
    val item = reference("item_id", Items, onDelete = ReferenceOption.CASCADE)
    val category = reference("category_id", Categories, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(item, category)
}

class CategoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryEntity>(Categories)

    var name by Categories.name
    var items by ItemEntity via ItemCategories
}