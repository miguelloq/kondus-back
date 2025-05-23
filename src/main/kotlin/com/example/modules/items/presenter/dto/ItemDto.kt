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
    val price: Double?,
    val quantity: Int?,
    val categoriesIds: List<Int>
){
    init{
        if(type != "produto" && type != "serviço" && type != "aluguel") throw ItemError.InvalidField("Type","can only be produto or serviço")
        if(price!= null && 0 > price) throw ItemError.InvalidField("Price","cannot be less than 0")
        if(quantity!=null && 0 > quantity) throw ItemError.InvalidField("Quantity","cannot be less than 0")
        if(title.length > 254) throw ItemError.InvalidField("Title length","cannot be more than 254 characters")
    }
}

@Serializable
data class ItemDto(
    val id: Int,
    val title: String,
    val description: String,
    val type: String,
    val price: Double?,
    val quantity: Int?,
    val categories: List<CategoryDto>,
    val imagesPaths: List<String>
)

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String
)

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val house: String,
    val local: String
)

fun ItemEntity.toItemDto() = ItemDto(
    id = id.value,
    title = title,
    description = description,
    type = type,
    price = price?.toDouble(),
    quantity = quantity,
    categories = categories.map { it.toCategoryDto() },
    imagesPaths = images.map { it.imagePath } ,
)

fun CategoryEntity.toCategoryDto() = CategoryDto(
    id = id.value,
    name = name
)

fun ItemEntity.toItemUserDto(): ItemUserDto{
    val itemUser = user
    val itemHouse = itemUser.house
    return ItemUserDto(
        item = toItemDto(),
        user = UserDto(
            id = itemUser.id.value,
            name = itemUser.name,
            house = itemHouse.description,
            local = itemHouse.local.name
        )
    )
}

