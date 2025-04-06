package com.example.modules.users.presenter.route

import com.example.core.models.CoreUser
import com.example.core.plugins.authentication.AuthenticationType
import com.example.core.plugins.authentication.getTokenConfig
import com.example.core.plugins.authentication.getUserId
import com.example.core.plugins.suspendTransaction
import com.example.core.presenter.extension.catchingHttp
import com.example.core.presenter.extension.catchingHttpAndId
import com.example.modules.locals.data.repository.HouseEntity
import com.example.modules.locals.data.repository.Houses
import com.example.modules.users.data.repository.UserEntity
import com.example.modules.users.data.repository.UserTable
import com.example.modules.users.presenter.dto.LoginRequestDto
import com.example.modules.users.presenter.dto.RegisterUserDto
import com.example.modules.users.domain.usecase.LoginUserUsecase
import com.example.modules.users.domain.usecase.RegisterUserUsecase
import com.example.modules.users.domain.error.UserError
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import kotlin.collections.mapNotNull

fun Route.usersRoute(
    registerUserUsecase: RegisterUserUsecase = application.inject<RegisterUserUsecase>().value,
    loginUserUsecase: LoginUserUsecase  = application.inject<LoginUserUsecase>().value
) = route("/users"){
    get("login"){
        catchingHttp<UserError> {
            val tokenConfig = application.getTokenConfig()
            val dto = call.receive<LoginRequestDto>()
            val token = loginUserUsecase(tokenConfig, dto)
            call.respond(hashMapOf("token" to token))
        }
    }

    post{
        catchingHttp<UserError>{
            val dto = call.receive<RegisterUserDto>()
            registerUserUsecase(dto)
            call.respond(HttpStatusCode.NoContent)
        }
    }

    authenticate(AuthenticationType.Core.value){
        get{
            catchingHttpAndId<UserError> { id ->
                call.respond(getLocalUsers(id))
            }
        }
        get("id"){
            catchingHttp<UserError> {
                val id = getUserId() ?: call.respond(HttpStatusCode.Unauthorized)
                call.respond(id)
            }
        }
        get("info"){
            catchingHttpAndId<UserError> { id ->
                val userInfoDto = getUserInfo(id)
                call.respond(userInfoDto)
            }
        }
    }
}

private suspend fun CoreUser.Id.toEntity() = suspendTransaction {
    UserEntity.find { UserTable.id eq value.toInt() }.firstOrNull() ?: throw UserError.NotFindedInDatabase
}

@Serializable data class UserInfoDto(val id:Int, val name: String, val house: HouseDto, val local: LocalDto)
@Serializable data class HouseDto(val id: Int, val name: String)
@Serializable data class LocalDto(val id: Int, val street: String, val number: Int, val postal: String, val name: String, val description: String)

private suspend fun getUserInfo(id: CoreUser.Id): UserInfoDto = suspendTransaction{
    val user = id.toEntity()
    val house = user.house
    val local = house.local
    UserInfoDto(
        id = user.id.value,
        name = user.name,
        house = HouseDto(
            id = house.id.value,
            name = house.description
        ),
        local = LocalDto(
            id = local.id.value,
            street = local.street,
            number = local.number,
            postal = local.postal,
            name = local.name,
            description = local.description
        )
    )
}

@Serializable data class LocalUserDto(val name: String, val id: Int, val houseName: String)
private fun UserEntity.toLocalUser() = LocalUserDto(name,id.value,house.description)
private suspend fun getLocalUsers(id: CoreUser.Id): List<LocalUserDto> = suspendTransaction{
    val userLocal = id.toEntity().house.local
    val allHouseIdsFromUserLocal = HouseEntity
        .find { Houses.localId eq userLocal.id.value }
        .map{ it.id.value }

    val allUsersFromUserLocal = UserEntity
        .find { UserTable.houseId inList allHouseIdsFromUserLocal }
        .mapNotNull {
            if(it.id.value == id.value.toInt()) null
            else it
        }
    allUsersFromUserLocal.map { it.toLocalUser() }
}