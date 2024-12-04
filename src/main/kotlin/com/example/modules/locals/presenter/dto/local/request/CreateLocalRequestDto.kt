package com.example.modules.locals.presenter.dto.local.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateLocalRequestDto(
    val street: String,
    val number: Int,
    val cep: Int,
    val name: String,
    val description: String,
    val category: String,
    val type: String
)