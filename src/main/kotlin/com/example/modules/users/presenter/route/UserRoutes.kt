package com.example.modules.users.presenter.route

import com.example.core.plugins.authentication.AuthenticationType
import com.example.core.plugins.authentication.getTokenConfig
import com.example.core.plugins.authentication.getUserId
import com.example.modules.users.presenter.dto.LoginRequestDto
import com.example.modules.users.presenter.dto.RegisterUserDto
import com.example.modules.users.domain.usecase.GetAllUserUsecase
import com.example.modules.users.domain.usecase.LoginUserUsecase
import com.example.modules.users.domain.usecase.RegisterUserUsecase
import com.example.modules.users.domain.error.UserError
import com.example.modules.users.domain.model.UserModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.usersRoute(
    getAllUserUsecase: GetAllUserUsecase = application.inject<GetAllUserUsecase>().value,
    registerUserUsecase: RegisterUserUsecase = application.inject<RegisterUserUsecase>().value,
    loginUserUsecase: LoginUserUsecase  = application.inject<LoginUserUsecase>().value
) = route("/users"){

    suspend fun RoutingContext.catchingUserError(block: suspend RoutingContext.()->Unit) = try{
        block()
    }catch(userErr: UserError){
        call.respond(HttpStatusCode.BadRequest,userErr.message)
    }catch (_: Exception){
        call.respond(HttpStatusCode.InternalServerError)
    }

    authenticate(AuthenticationType.Core.value){
        get(){
            catchingUserError {
                val users = getAllUserUsecase()
                val response = users.map{ it.toGetAllUser() }
                call.respond(response)
            }
        }
    }

    get("login"){
        catchingUserError {
            val tokenConfig = application.getTokenConfig()
            val dto = call.receive<LoginRequestDto>()
            val token = loginUserUsecase(tokenConfig, dto)
            call.respond(hashMapOf("token" to token))
        }
    }

    post{
        catchingUserError {
            val dto = call.receive<RegisterUserDto>()
            registerUserUsecase(dto)
            call.respond(HttpStatusCode.NoContent)
        }
    }

    authenticate(AuthenticationType.Core.value){
        get("id"){
            catchingUserError {
                val id = getUserId() ?: call.respond(HttpStatusCode.Unauthorized)
                call.respond(id)
            }
        }
    }
}

@Serializable data class GetUserResponseDto(val id:Long, val name: String)
fun Pair<Long, UserModel>.toGetAllUser() = GetUserResponseDto(first, second.name.value)