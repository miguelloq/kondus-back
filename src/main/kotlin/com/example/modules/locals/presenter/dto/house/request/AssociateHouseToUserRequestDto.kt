package com.example.modules.locals.presenter.dto.house.request

import kotlinx.serialization.Serializable

@Serializable
data class AssociateHouseToUserRequestDto (
    val houseId: Long,
    val residentId: Long
)