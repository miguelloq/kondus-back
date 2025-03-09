package com.example.modules.locals.data.repository

import com.example.core.models.CoreUser
import com.example.core.plugins.suspendTransaction
import com.example.core.presenter.dto.IdDomainModelWrapDto
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.model.HouseModel
import com.example.modules.locals.domain.repository.HouseRepository
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.users.data.repository.UserEntity
import com.example.modules.users.data.repository.UserTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class HouseRepositoryImpl(
    private val localRepository: LocalRepository
): HouseRepository {
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

    override suspend fun getLocalId(houseId: Long): Long? = suspendTransaction(){
        Houses
            .select(Houses.id,Houses.localId)
            .where{ Houses.id eq houseId.toInt() }
            .singleOrNull()
            ?.let{ it[Houses.localId].toLong() }
    }
}