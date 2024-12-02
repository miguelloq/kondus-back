package com.example.modules.locals.domain.model

import com.example.modules.locals.domain.error.LocalError
import kotlinx.serialization.Serializable

@Serializable
data class HouseModel(
    val name: Name,
    val local: LocalModel
){
    @Serializable @JvmInline value class Name(val s: String){
        init{
            val maximumLength = 20
            if(s.isEmpty()) throw LocalError.InvalidField("Name", "cannot be blank.")
            if(s.length>maximumLength) throw LocalError.InvalidField("Name", "the maximum length is $maximumLength.")
        }
    }
}