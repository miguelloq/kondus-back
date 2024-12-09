package com.example.modules.locals

import com.example.modules.locals.data.repository.HouseRepositoryImpl
import com.example.modules.locals.data.repository.LocalRepositoryImpl
import com.example.modules.locals.domain.repository.HouseRepository
import com.example.modules.locals.domain.repository.LocalRepository
import com.example.modules.locals.domain.usecase.AssociateHouseToUserUsecase
import com.example.modules.locals.domain.usecase.CreateHouseUsecase
import com.example.modules.locals.domain.usecase.CreateLocalUsecase
import com.example.modules.locals.domain.usecase.GetAllHousesFromUserUsecase
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.localKoinModule() = module{
    single<LocalRepository>{ LocalRepositoryImpl() }
    single<HouseRepository>{ HouseRepositoryImpl(get()) }
    single{ CreateLocalUsecase(get()) }
    single{ CreateHouseUsecase(get(),get()) }
    single{ AssociateHouseToUserUsecase(get(), get()) }
    single{ GetAllHousesFromUserUsecase(get()) }
}