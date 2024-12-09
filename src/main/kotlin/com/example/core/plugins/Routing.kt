package com.example.core.plugins

import com.example.modules.locals.presenter.route.housesRoutes
import com.example.modules.locals.presenter.route.localsRoute
import com.example.modules.users.presenter.route.usersRoute
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

fun Application.configureRouting(){
    install(ContentNegotiation) { json() }
    routing{
        usersRoute()
        localsRoute()
        housesRoutes()
    }
}