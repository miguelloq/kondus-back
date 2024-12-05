package com.example.modules.locals.domain.usecase

import com.example.core.models.CoreUser
import com.example.core.presenter.dto.IdDomainModelWrapDto
import com.example.modules.locals.domain.model.HouseModel
import com.example.modules.locals.domain.repository.HouseRepository

class GetAllHousesFromUserUsecase(
    private val repository: HouseRepository
) {
    suspend operator fun invoke(id: CoreUser.Id): List<IdDomainModelWrapDto<Long, HouseModel>> =
        repository.getHousesFromUser(id)
}