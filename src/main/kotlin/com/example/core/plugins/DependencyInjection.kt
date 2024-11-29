package com.example.core.plugins

import com.example.core.coreKoinModule
import com.example.modules.users.userKoinModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.KoinApplication
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection(){
    install(Koin) {
        slf4jLogger()
        configureKoinModules()
    }
}

fun KoinApplication.configureKoinModules(){
    modules(
        coreKoinModule(),
        userKoinModule()
    )
}
