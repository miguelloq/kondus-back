package com.example.modules.users

import com.example.modules.locals.data.repository.LocalRepositoryImpl
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.users.data.repository.UserRepositoryFake
import com.example.modules.users.data.repository.UserRepositoryImpl
import com.example.modules.users.data.service.CreateTokenServiceImpl
import com.example.modules.users.domain.repository.UserRepository
import com.example.modules.users.domain.service.CreateTokenService
import com.example.modules.users.domain.usecase.GetAllUserUsecase
import com.example.modules.users.domain.usecase.LoginUserUsecase
import com.example.modules.users.domain.usecase.RegisterUserUsecase
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.userKoinModule() = module{
    single<UserRepository>{ UserRepositoryImpl() }
    //single<UserRepository>{ UserRepositoryFake() }
    single<CreateTokenService>{ CreateTokenServiceImpl() }
    single<LocalRepository>{ LocalRepositoryImpl() }
    single{ RegisterUserUsecase(get(),get()) }
    single{ GetAllUserUsecase(get()) }
    single{ LoginUserUsecase(get(),get()) }
}