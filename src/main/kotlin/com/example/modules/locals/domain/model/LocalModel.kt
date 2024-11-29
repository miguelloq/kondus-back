package com.example.modules.locals.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocalModel(
    val name: Name,
    val description: Description,
    val category: Category,
    val address: Address,
){
    @JvmInline @Serializable value class Name(val s: String)
    @JvmInline @Serializable value class Description(val s: String)

    @Serializable
    sealed interface Category{
        data object Apartment: Category
        data object Condominum: Category
    }

    @Serializable
    data class Address(
        val street: Street,
        val number: Number,
        val cep: Cep,
    ){
        @JvmInline @Serializable value class Street(val s: String)
        @JvmInline @Serializable value class Number(val s: String)
        @JvmInline @Serializable value class Cep(val s: String)
    }
}