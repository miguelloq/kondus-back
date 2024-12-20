package com.example.modules.locals.domain.usecase

import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.model.HouseModel
import com.example.modules.locals.domain.model.LocalModel
import com.example.modules.locals.domain.repository.HouseRepository
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.locals.presenter.dto.house.request.CreateHouseRequestDto

class CreateHouseUsecase(
    private val houseRepository: HouseRepository,
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(dto: CreateHouseRequestDto): Long{
        val local = localRepository.get(dto.localId) ?: throw LocalError.LocalDontExists
        val house = dto.toDomain(local)
        val houseId = houseRepository.create(house,dto.localId)
        return houseId
    }
}

private fun CreateHouseRequestDto.toDomain(local: LocalModel) = HouseModel(
    name = HouseModel.Name(description),
    local = local
)