package com.example.modules.locals.presenter.route

import com.example.core.plugins.authentication.AuthenticationType
import com.example.core.presenter.dto.IdDomainModelWrapDto
import com.example.core.presenter.dto.RequestWrapDto
import com.example.core.presenter.extension.catchingHttp
import com.example.core.presenter.extension.catchingHttpAndId
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.model.HouseModel
import com.example.modules.locals.domain.usecase.AssociateHouseToUserUsecase
import com.example.modules.locals.domain.usecase.CreateHouseUsecase
import com.example.modules.locals.domain.usecase.GetAllHousesFromUserUsecase
import com.example.modules.locals.presenter.dto.house.request.AssociateHouseToUserRequestDto
import com.example.modules.locals.presenter.dto.house.request.CreateHouseRequestDto
import com.example.modules.locals.presenter.dto.house.response.CreateHouseResponseDto
import com.example.modules.locals.presenter.dto.local.response.GetAllHousesFromUserResponseDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.housesRoutes(
    createHouseUsecase: CreateHouseUsecase = application.inject<CreateHouseUsecase>().value,
    getAllHousesFromUserUsecase: GetAllHousesFromUserUsecase = application.inject<GetAllHousesFromUserUsecase>().value,
    associateHouseToUserUsecase: AssociateHouseToUserUsecase = application.inject<AssociateHouseToUserUsecase>().value
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

        get{//Only local residents
            catchingHttpAndId<LocalError>(){ id ->
                val houses = getAllHousesFromUserUsecase(id)
                val response = houses.map{ it.toResponse() }
                call.respond(response)
            }
        }

        put("/user"){//Only local owners
            catchingHttpAndId<LocalError>(){ localOwnerId ->
                val request = call.receive<AssociateHouseToUserRequestDto>()
                val wrap = RequestWrapDto(request,localOwnerId)
                associateHouseToUserUsecase(wrap)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}

private fun IdDomainModelWrapDto<Long, HouseModel>.toResponse() = let{ (id,model) ->
    GetAllHousesFromUserResponseDto(id, model)
}