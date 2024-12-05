package com.example.modules.locals.domain.usecase

import com.example.core.presenter.dto.RequestWrapDto
import com.example.modules.locals.domain.model.HouseModel
import com.example.modules.locals.domain.model.LocalModel
import com.example.modules.locals.domain.repository.HouseRepository
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.locals.domain.usecase.toDomain
import com.example.modules.locals.presenter.dto.house.request.CreateHouseRequestDto

class CreateHouseUsecase(
    private val houseRepository: HouseRepository,
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(dto: RequestWrapDto<CreateHouseRequestDto>): Long{
        val (request,id) = dto
        val local = localRepository.get(request.localId)
        val house = request.toDomain(local)
        val houseId = houseRepository.create(house,id)
        return houseId
    }
}

private fun CreateHouseRequestDto.toDomain(local: LocalModel) = HouseModel(
    name = HouseModel.Name(description),
    local = local
)