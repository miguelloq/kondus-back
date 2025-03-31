package com.example.modules.items.domain

enum class ItemTypeEnum{ Product,Loan,Service }
fun String.toItemTypeEnum(
    onError: () -> Nothing
) = when(this){
    "produto" -> ItemTypeEnum.Product
    "aluguel" -> ItemTypeEnum.Loan
    "serviço" -> ItemTypeEnum.Service
    else -> onError()
}