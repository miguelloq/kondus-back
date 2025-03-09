package com.example.modules.locals.presenter.route

import com.example.core.plugins.authentication.AuthenticationType
import com.example.core.presenter.extension.catchingHttp
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.usecase.CreateHouseUsecase
import com.example.modules.locals.presenter.dto.house.request.CreateHouseRequestDto
import com.example.modules.locals.presenter.dto.house.response.CreateHouseResponseDto
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.housesRoutes(
    createHouseUsecase: CreateHouseUsecase = application.inject<CreateHouseUsecase>().value,
) = route("houses"){

    authenticate(AuthenticationType.Core.value){
        post{//Only local owners
            catchingHttp<LocalError>(){
                val request = call.receive<CreateHouseRequestDto>()
                val houseId = createHouseUsecase(request)
                val response = CreateHouseResponseDto(houseId)
                call.respond(response)
            }
        }
    }
}
