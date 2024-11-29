package com.example.modules.users.domain.usecase

import com.example.modules.users.domain.model.UserModel
import com.example.modules.users.domain.repository.UserRepository

class GetAllUserUsecase(val userRepo: UserRepository) {
    suspend operator fun invoke(): List<Pair<Long,UserModel>> =
        userRepo.all()
}