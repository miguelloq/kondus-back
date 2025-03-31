package com.example.modules.items.database.repository

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


object ItemImages : IntIdTable("item_image") {
    val item = reference("item_id", Items, onDelete = ReferenceOption.CASCADE)
    val imagePath = text("image_path")
}
class ItemImageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemImageEntity>(ItemImages)

    var item by ItemEntity referencedOn ItemImages.item
    var imagePath by ItemImages.imagePath
}



