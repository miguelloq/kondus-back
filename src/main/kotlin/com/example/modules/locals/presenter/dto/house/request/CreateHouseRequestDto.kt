package com.example.modules.locals.presenter.dto.house.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateHouseRequestDto(
    val description: String,
    val localId: Long,
)
