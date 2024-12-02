package com.example.modules.locals.presenter.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateLocalRequestDto(
    val street: String,
    val number: Int,
    val cep: Int,
    val name: String,
    val description: String,
    val type: String
)

@Serializable
data class CreateLocalDto(
    val userId: Long,
    val requestDto: CreateLocalRequestDto
)
