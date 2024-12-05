package com.example.modules.locals.data.repository

import com.example.core.models.CoreUser
import com.example.core.plugins.suspendTransaction
import com.example.core.presenter.dto.IdDomainModelWrapDto
import com.example.modules.locals.domain.model.HouseModel
import com.example.modules.locals.domain.repository.HouseRepository
import org.jetbrains.exposed.sql.insert

class HouseRepositoryImpl: HouseRepository {
    override suspend fun create(
        model: HouseModel,
        localId: Long,
    ): Long = suspendTransaction{
        val id = Houses.insert{
            it[description] = model.name.s
            it[Houses.localId] = localId.toInt()
        } get Houses.id

        id.value.toLong()
    }

    override suspend fun getHousesFromUser(userId: CoreUser.Id): List<IdDomainModelWrapDto<Long, HouseModel>> = suspendTransaction{
        TODO("Not yet implemented")
    }
}