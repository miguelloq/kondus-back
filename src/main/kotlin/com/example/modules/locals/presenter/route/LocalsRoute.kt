package com.example.modules.locals.presenter.route

import com.example.core.plugins.authentication.AuthenticationType
import com.example.core.presenter.dto.RequestWrapDto
import com.example.core.presenter.extension.catchingHttpAndId
import com.example.modules.locals.domain.error.LocalError
import com.example.modules.locals.domain.usecase.CreateLocalUsecase
import com.example.modules.locals.presenter.dto.local.request.CreateLocalRequestDto
import com.example.modules.locals.presenter.dto.local.response.CreateLocalResponseDto
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.application
import org.koin.ktor.ext.inject

fun Route.localsRoute(
    createLocalUsecase: CreateLocalUsecase = application.inject<CreateLocalUsecase>().value
) = route("locals"){
    authenticate(AuthenticationType.Core.value){
        post{
            catchingHttpAndId<LocalError>(){ id ->
                val request = call.receive<CreateLocalRequestDto>()
                val id = createLocalUsecase(request)
                call.respond(CreateLocalResponseDto(id))
            }
        }
    }
}