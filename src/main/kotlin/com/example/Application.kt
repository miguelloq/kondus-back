package com.example

import com.example.core.plugins.authentication.configureAuthentication
import com.example.core.plugins.aws.configureAws
import com.example.core.plugins.configureDatabase
import com.example.core.plugins.configureDependencyInjection
import com.example.core.plugins.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureAws()
    configureDependencyInjection()
    configureAuthentication()
    configureRouting()
}
