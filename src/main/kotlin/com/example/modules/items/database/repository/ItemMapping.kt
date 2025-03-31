package com.example.modules.items.database.repository

import com.example.modules.items.domain.ItemTypeEnum
import com.example.modules.users.data.repository.UserEntity
import com.example.modules.users.data.repository.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Items : IntIdTable("items") {
    val title = varchar("title", 255)
    val description = text("description")
    val type = varchar("type", 20)
    val price = decimal("price", 10, 2).nullable()
    val quantity = integer("quantity").nullable()
    val user = reference("user_id", UserTable.id, onDelete = ReferenceOption.CASCADE)
}

class ItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemEntity>(Items)

    var title by Items.title
    var description by Items.description
    var type by Items.type
    var price by Items.price
    var quantity by Items.quantity
    var user by UserEntity referencedOn Items.user
    var categories by CategoryEntity via ItemCategories
    val images: List<ItemImageEntity>
        get() = ItemImageEntity.find { ItemImages.item eq id }.toList()
    val itemTypeEnum: ItemTypeEnum get() = when{
        type == "produto" && quantity == 0 -> ItemTypeEnum.Loan
        type == "produto" -> ItemTypeEnum.Product
        type == "serviço" -> ItemTypeEnum.Service
        else -> ItemTypeEnum.Product
    }
}