package com.example.modules.locals.presenter.route

import com.example.core.models.AuthenticationType
import com.example.core.models.CoreUser
import com.example.core.models.RequestWrapDto
import com.example.core.plugins.getUserId
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.usecase.CreateHouseUsecase
import com.example.modules.locals.presenter.dto.house.request.CreateHouseRequestDto
import com.example.modules.locals.presenter.dto.house.response.CreateHouseResponseDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.housesRoutes(
    createHouseUsecase: CreateHouseUsecase = application.inject<CreateHouseUsecase>().value
) = route("houses"){

    suspend fun RoutingContext.catchingLocalError(block: suspend RoutingContext.()->Unit) = try{
        block()
    }catch(localErr: LocalError){
        call.respond(HttpStatusCode.BadRequest,localErr.message)
    }catch (_: Exception){
        call.respond(HttpStatusCode.InternalServerError)
    }

    authenticate(AuthenticationType.Core.value){
        post{
            catchingLocalError{
                val userId = getUserId()
                    ?: call.respond(HttpStatusCode.Unauthorized).let{ return@catchingLocalError }

                val request = call.receive<CreateHouseRequestDto>()
                val wrap = RequestWrapDto(request,CoreUser.Id(userId))
                val houseId = createHouseUsecase(wrap)
                call.respond(CreateHouseResponseDto(houseId))
            }
        }

        get("/all"){}

        put("/user"){}
    }
}