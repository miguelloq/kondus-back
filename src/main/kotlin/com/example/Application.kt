package com.example

import com.example.core.plugins.authentication.configureAuthentication
import com.example.core.plugins.configureDependencyInjection
import com.example.core.plugins.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //configureDatabase()
    configureDependencyInjection()
    configureAuthentication()
    configureRouting()
}
