package com.example.core.models

import kotlinx.serialization.Serializable

@Serializable
data class CoreUser(
    val id: Id
){
    @Serializable @JvmInline value class Id(val value: Long)
}
