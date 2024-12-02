package com.example.modules.locals.domain.usecase

import com.example.modules.locals.domain.model.LocalModel
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.locals.presenter.dto.CreateLocalDto
import com.example.modules.locals.presenter.dto.CreateLocalRequestDto

class CreateLocalUsecase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(dto: CreateLocalDto): Long{
        val (userId, response) = dto
        val model = response.toDomain()
        val id = repository.create(model,userId)
        return id
    }
}

private fun CreateLocalRequestDto.toDomain() = LocalModel(
    name = LocalModel.Name(name),
    description = LocalModel.Description(description),
    category = LocalModel.Category.of(category),
    address = LocalModel.Address(
        cep = LocalModel.Address.Cep(cep),
        street = LocalModel.Address.Street(street),
        number = LocalModel.Address.Number(number)
    )
)