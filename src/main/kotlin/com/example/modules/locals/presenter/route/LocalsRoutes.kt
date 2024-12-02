package com.example.modules.locals.presenter.route

import com.example.core.models.AuthenticationType
import com.example.core.plugins.getUserId
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.usecase.CreateLocalUsecase
import com.example.modules.locals.presenter.dto.CreateLocalDto
import com.example.modules.locals.presenter.dto.CreateLocalRequestDto
import com.example.modules.locals.presenter.dto.CreateLocalResponseDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.application
import org.koin.ktor.ext.inject

fun Route.localsRoutes(
    createLocalUsecase: CreateLocalUsecase = application.inject<CreateLocalUsecase>().value
) = route("locals"){
    authenticate(AuthenticationType.Core.value){
        post{
            try{
                val userId = getUserId()
                    ?: call.respond(HttpStatusCode.Unauthorized).let{ return@post }

                val request = call.receive<CreateLocalRequestDto>()
                val id = createLocalUsecase(CreateLocalDto(userId,request))
                call.respond(CreateLocalResponseDto(id))
            }catch(localErr: LocalError){
                call.respond(HttpStatusCode.BadRequest,localErr.message)
            }catch(_: Exception){
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}