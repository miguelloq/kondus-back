package com.example.modules.locals.domain.usecase

import com.example.core.models.RequestWrapDto
import com.example.modules.locals.domain.repository.HouseRepository
import com.example.modules.locals.presenter.dto.house.request.CreateHouseRequestDto

class CreateHouseUsecase(
    private val repository: HouseRepository
) {
    suspend operator fun invoke(dto: RequestWrapDto<CreateHouseRequestDto>): Long = 0
}