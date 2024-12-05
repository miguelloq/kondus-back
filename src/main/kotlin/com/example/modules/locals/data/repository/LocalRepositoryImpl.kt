package com.example.modules.locals.data.repository

import com.example.core.plugins.suspendTransaction
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.model.LocalModel
import com.example.modules.locals.domain.repository.LocalRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import kotlin.collections.get

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

    override suspend fun get(localId: Long): LocalModel? = Locals
        .selectAll()
        .where{ Locals.id eq localId.toInt()}
        .fold(emptyList<LocalModel>()){ acc, it ->
            acc + LocalModel(
                name = LocalModel.Name(it[Locals.name]),
                description = LocalModel.Description(it[Locals.description]),
                address = LocalModel.Address(
                    street = LocalModel.Address.Street(it[Locals.street]),
                    cep = LocalModel.Address.Cep(it[Locals.postal].toInt()),
                    number = LocalModel.Address.Number(it[Locals.number])
                ),
                category = when(it[Locals.type]){
                    "Apartment" -> LocalModel.Category.Condominium
                    "Condominium" -> LocalModel.Category.Apartment
                    else -> throw LocalError.InvalidLocalCategory
                }
            )
        }
        .singleOrNull()
}

private fun LocalModel.Category.fromDatabaseString(s: String) = when(s){
    "Apartment" -> LocalModel.Category.Condominium
    "Condominium" -> LocalModel.Category.Apartment
    else -> Exception()
}

private fun LocalModel.Category.toDatabaseString() = when(this){
    LocalModel.Category.Apartment -> "Apartment"
    LocalModel.Category.Condominium -> "Condominium"
}