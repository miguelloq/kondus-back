package com.example.modules.users.domain.usecase

import com.example.modules.users.presenter.dto.RegisterUserDto
import com.example.modules.users.domain.model.UserModel
import com.example.modules.users.domain.error.UserError
import com.example.modules.users.domain.model.UserModel.Email
import com.example.modules.users.domain.model.UserModel.Password
import com.example.modules.users.domain.model.UserModel.Name
import com.example.modules.users.domain.repository.UserRepository

class RegisterUserUsecase(val userRepo: UserRepository) {
    suspend operator fun invoke(dto: RegisterUserDto){
        val model = dto.toModel()
        val isEmailAlreadyInUse = userRepo.findByEmail(model.email) != null
        if(isEmailAlreadyInUse) throw UserError.EmailAlreadyRegistered
        userRepo.create(model)
    }
}

private fun RegisterUserDto.toModel() = UserModel(
    name = Name(name),
    email = Email(email),
    password = Password(password)
)