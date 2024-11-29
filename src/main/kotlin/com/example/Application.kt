package com.example

import com.example.core.plugins.configureAuthentication
import com.example.core.plugins.configureDatabase
import com.example.core.plugins.configureDependecyInjection
import com.example.core.plugins.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //configureDatabase()
    configureDependecyInjection()
    configureAuthentication()
    configureRouting()
}
