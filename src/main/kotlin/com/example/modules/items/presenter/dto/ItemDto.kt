package com.example.modules.items.presenter.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val name: String,
    val description: String,
    val imageUrl: List<String>,
    val user: UserDto
)

@Serializable
data class UserDto(val name: String, val house: String)
