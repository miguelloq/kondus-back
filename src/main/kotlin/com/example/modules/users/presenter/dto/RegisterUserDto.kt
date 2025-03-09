package com.example.modules.users.presenter.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto(
    val name: String,
    val email: String,
    val password: String,
    val houseId: Int
)