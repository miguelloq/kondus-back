package com.example.modules.items

import com.example.modules.items.database.repository.AwsService
import com.example.modules.items.database.repository.ItemRepository
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.itemKoinModule() = module{
    single{ AwsService() }
    single<ItemRepository>{ ItemRepository(get()) }
}