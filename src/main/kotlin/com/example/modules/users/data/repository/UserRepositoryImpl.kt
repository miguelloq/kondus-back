package com.example.modules.users.data.repository


import com.example.core.plugins.suspendTransaction
import com.example.modules.users.domain.error.UserError
import com.example.modules.users.domain.model.UserModel
import com.example.modules.users.domain.model.UserModel.Email
import com.example.modules.users.domain.model.UserModel.Name
import com.example.modules.users.domain.model.UserModel.Password
import com.example.modules.users.domain.repository.UserRepository

class UserRepositoryImpl(): UserRepository {
    override suspend fun create(userModel: UserModel): Unit = suspendTransaction{
        UserEntity.new {
            name = userModel.name.value
            email = userModel.email.value
            password = userModel.password.value
            houseId = userModel.houseId
        }
    }

    override suspend fun all(): List<Pair<Long,UserModel>> = suspendTransaction {
        UserEntity
            .all()
            .fold(emptyList<Pair<Long,UserModel>>()){ acc, head ->
                try{ acc + Pair(head.id.value.toLong(),head.toModel()) }
                catch (e: UserError){ acc }
            }
    }

    override suspend fun findByEmail(email: Email): Pair<Long,UserModel>? = suspendTransaction {
        UserEntity
            .find{ UserTable.email eq email.value}
            .limit(1)
            .map { Pair(it.id.value.toLong(), it.toModel()) }
            .firstOrNull()

    }

    override suspend fun findById(id: Long): UserModel?  = suspendTransaction {
        UserEntity
            .findById(id.toInt())
            ?.toModel()
    }
}

private fun UserEntity.toModel() = UserModel(
    email = Email(email),
    name = Name(name),
    password = Password(password),
    houseId = houseId
)