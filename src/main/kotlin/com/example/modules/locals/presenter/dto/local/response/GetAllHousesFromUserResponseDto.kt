package com.example.modules.locals.presenter.dto.local.response

import com.example.modules.locals.domain.model.HouseModel
import kotlinx.serialization.Serializable

@Serializable
data class GetAllHousesFromUserResponseDto(
    val id: Long,
    val model: HouseModel
)