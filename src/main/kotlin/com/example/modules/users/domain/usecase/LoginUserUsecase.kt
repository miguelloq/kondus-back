package com.example.modules.users.domain.usecase

import com.example.core.model.TokenConfig
import com.example.modules.users.presenter.dto.LoginRequestDto
import com.example.modules.users.domain.error.UserError
import com.example.modules.users.domain.model.UserModel.Email
import com.example.modules.users.domain.model.UserModel.Password
import com.example.modules.users.domain.repository.UserRepository
import com.example.modules.users.domain.service.CreateTokenService

class LoginUserUsecase(
    val userRepo: UserRepository,
    val createTokenService: CreateTokenService
) {
    suspend operator fun invoke(tokenConfig: TokenConfig,dto: LoginRequestDto): String{
        val (email, password) = dto.toDomain()

        val (id,repoUser) = userRepo.findByEmail(email)
            ?: throw UserError.NotFindedInDatabase

        if(repoUser.password.value.also(::println) != password.value.also(::println))
            throw UserError.WrongPassword

        return createTokenService.getToken(id,tokenConfig)
    }
}

private fun LoginRequestDto.toDomain() = Pair(
    Email(email),
    Password(password)
)