package com.example.modules.locals.domain.repository

import com.example.core.models.CoreUser
import com.example.core.presenter.dto.IdDomainModelWrapDto
import com.example.modules.locals.domain.model.HouseModel

interface HouseRepository{
    suspend fun create(model: HouseModel, localId: Long): Long
    suspend fun getLocalId(houseId: Long): Long?
}