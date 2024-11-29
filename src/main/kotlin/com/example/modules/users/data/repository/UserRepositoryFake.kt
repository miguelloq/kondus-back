package com.example.modules.users.data.repository

import com.example.modules.users.domain.model.UserModel
import com.example.modules.users.domain.repository.UserRepository

class UserRepositoryFake: UserRepository {
    private val userModels = mutableListOf<UserModel>()

    override suspend fun create(userModel: UserModel) {
        userModels.add(userModel)
    }

    override suspend fun all(): List<Pair<Long, UserModel>> =
        userModels
            .withIndex()
            .map{(idx,user) -> idx.toLong() to user}

    override suspend fun findByEmail(email: UserModel.Email): Pair<Long, UserModel>? =
        all().firstOrNull { (_, user) -> user.email == email }

    override suspend fun findById(id: Long): UserModel? =
        all().firstOrNull { (idUser, _) -> idUser == id }
            ?.second
}