package com.example.modules.items.presenter

import com.example.core.plugins.authentication.AuthenticationType
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.get

fun Route.itemRoutes() = route("items"){
    authenticate(AuthenticationType.Core.value) {
        get(){
            val userId = call.parameters["id"]

        }
    }
}