package com.example.modules.locals.data.repository

import com.example.core.plugins.suspendTransaction
import com.example.modules.locals.domain.model.LocalModel
import com.example.modules.locals.domain.repository.LocalRepository
import org.jetbrains.exposed.sql.insert

class LocalRepositoryImpl: LocalRepository {
    override suspend fun create(
        model: LocalModel,
        creatorUserId: Long
    ): Long = suspendTransaction{
        val id = Locals.insert{
            it[street] = model.address.street.s
            it[number] = model.address.number.n
            it[postal] = model.address.cep.n.toString()
            it[name] = model.name.s
            it[description] = model.description.s
            it[userId] = userId
            it[type] = model.category.toDatabaseString()
        } get Locals.id

        id.value.toLong()
    }
}

private fun LocalModel.Category.toDatabaseString() = when(this){
    LocalModel.Category.Apartment -> "Apartment"
    LocalModel.Category.Condominium -> "Condominium"
}