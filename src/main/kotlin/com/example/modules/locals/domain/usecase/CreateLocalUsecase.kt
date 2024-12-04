package com.example.modules.locals.domain.usecase

import com.example.core.models.RequestWrapDto
import com.example.modules.locals.domain.model.LocalModel
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.locals.presenter.dto.local.request.CreateLocalRequestDto

class CreateLocalUsecase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(dto: RequestWrapDto<CreateLocalRequestDto>): Long{
        val (response,userId) = dto
        val model = response.toDomain()
        val id = repository.create(model,userId.value)
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