package com.example.modules.users.domain.repository

import com.example.modules.users.domain.model.UserModel

interface UserRepository {
    suspend fun create(userModel: UserModel)
    suspend fun all(): List<Pair<Long,UserModel>>
    suspend fun findByEmail(email: UserModel.Email): Pair<Long,UserModel>?
    suspend fun findById(id:Long): UserModel?
}