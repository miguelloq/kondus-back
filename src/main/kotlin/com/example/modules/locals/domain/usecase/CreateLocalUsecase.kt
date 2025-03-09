package com.example.modules.locals.domain.usecase

import com.example.core.presenter.dto.RequestWrapDto
import com.example.modules.locals.domain.model.LocalModel
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.locals.presenter.dto.local.request.CreateLocalRequestDto

class CreateLocalUsecase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(dto: CreateLocalRequestDto): Long{
        val model = dto.toDomain()
        val id = repository.create(model)
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