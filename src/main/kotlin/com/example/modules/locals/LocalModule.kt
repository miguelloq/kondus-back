package com.example.modules.locals

import com.example.modules.locals.data.repository.LocalRepositoryImpl
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.locals.domain.usecase.CreateLocalUsecase
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.localKoinModule() = module{
    single<LocalRepository>{ LocalRepositoryImpl() }
    single{ CreateLocalUsecase(get()) }
}