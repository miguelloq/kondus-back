package com.example.modules.locals.data.repository

import com.example.core.models.CoreUser
import com.example.core.plugins.suspendTransaction
import com.example.core.presenter.dto.IdDomainModelWrapDto
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.model.HouseModel
import com.example.modules.locals.domain.repository.HouseRepository
import com.example.modules.locals.domain.repository.LocalRepository
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

    //TODO: Demands more tests
    override suspend fun getHousesFromUser(userId: CoreUser.Id): List<IdDomainModelWrapDto<Long, HouseModel>> = suspendTransaction{
        (Houses leftJoin UsersHouses)
            .selectAll()
            .where{
                UsersHouses.userId.eq(userId.value.toInt()) and
                        Houses.id.eq(UsersHouses.houseId)
            }
            .map{
                IdDomainModelWrapDto(
                    id = it[Houses.id].value.toLong(),
                    domainModel = HouseModel(
                        name = HouseModel.Name(it[Houses.description]),
                        local = localRepository.get(it[Houses.localId].toLong())
                            ?: throw LocalError.LocalDontExists
                    )
                )
            }
    }

    override suspend fun associateHouseToUser(userId: CoreUser.Id, houseId: Long) = suspendTransaction(){
        UsersHouses.insert{
            it[UsersHouses.userId] = userId.value.toInt()
            it[UsersHouses.houseId] = houseId.toInt()
        }
        Unit
    }

    override suspend fun getLocalId(houseId: Long): Long? = suspendTransaction(){
        Houses
            .select(Houses.id,Houses.localId)
            .where{ Houses.id eq houseId.toInt() }
            .singleOrNull()
            ?.let{ it[Houses.localId].toLong() }
    }
}