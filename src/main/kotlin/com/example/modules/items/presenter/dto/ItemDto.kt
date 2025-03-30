package com.example.modules.items.presenter.dto

import com.example.modules.items.database.repository.CategoryEntity
import com.example.modules.items.database.repository.ItemEntity
import com.example.modules.items.domain.ItemError
import kotlinx.serialization.Serializable

@Serializable
data class ItemUserDto(
    val item: ItemDto,
    val user: UserDto
)

@Serializable
data class CreateItemDto(
    val title: String,
    val description: String,
    val type: String,
    val price: Double,
    val quantity: Int,
    val categories: List<Int>
){
    init{
        if(type != "produto" && type != "serviço") throw ItemError.InvalidField("Type","can only be produto or serviço")
        if(0.01 > price) throw ItemError.InvalidField("Price","cannot be less than 0.01")
        if(0 > quantity) throw ItemError.InvalidField("Quantity","cannot be less than 1")
        if(title.length > 254) throw ItemError.InvalidField("Title length","cannot be more than 254 characters")
    }
}

@Serializable
data class ItemDto(
    val id: Int,
    val title: String,
    val description: String,
    val type: String,
    val price: Double,
    val quantity: Int,
    val categories: List<CategoryDto>
)

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String
)

@Serializable
data class UserDto(
    val name: String,
    val house: String
)

fun ItemEntity.toItemDto() = ItemDto(
    id = id.value,
    title = title,
    description = description,
    type = type,
    price = price?.toDouble() ?: 0.0,
    quantity = quantity ?: 0,
    categories = categories.map{ it.toCategoryDto() }
)

fun CategoryEntity.toCategoryDto() = CategoryDto(
    id = id.value,
    name = name
)

fun ItemEntity.toItemUserDto() = ItemUserDto(
    item = toItemDto(),
    user = UserDto(
        name = user.name,
        house = user.house.description
    )
)