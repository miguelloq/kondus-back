package com.example.modules.locals.domain.usecase

import com.example.core.models.CoreUser
import com.example.core.presenter.dto.RequestWrapDto
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.repository.HouseRepository
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.locals.presenter.dto.house.request.AssociateHouseToUserRequestDto

class AssociateHouseToUserUsecase(
    private val houseRepository: HouseRepository,
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(wrap: RequestWrapDto<AssociateHouseToUserRequestDto>){
        val (request, userId) = wrap
        val (houseId, residentId) = request

        val localId = houseRepository.getLocalId(request.houseId) ?: throw LocalError.HouseDontExists
        if(!localRepository.userIsLocalOwner(userId,localId)) throw LocalError.UserDontOwnLocal

        houseRepository.associateHouseToUser(CoreUser.Id(residentId),houseId)
    }
}