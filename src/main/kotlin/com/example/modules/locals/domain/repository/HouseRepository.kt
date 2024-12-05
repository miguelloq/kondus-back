package com.example.modules.locals.domain.repository

import com.example.core.models.CoreUser
import com.example.modules.locals.domain.model.HouseModel

interface HouseRepository{
    suspend fun create(model: HouseModel, creator: CoreUser.Id): Long
    suspend fun getHouseFromUser(userId: CoreUser.Id)
}