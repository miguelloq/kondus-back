package com.example.modules.users.domain.model

import com.example.modules.users.domain.error.UserError
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val name: Name,
    val email: Email,
    val password: Password,
){
    @JvmInline @Serializable
    value class Name(val value: String){
        init{
            value.maximumLength("Name",20)
            value.cannotByBlank("Name")
        }
    }

    @JvmInline @Serializable
    value class Email(val value: String){
        init{
            value.maximumLength("Email",40)
            value.cannotByBlank("Email")

            val funIsEmail = {s: String -> "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex().matches(s)}
            if(!funIsEmail(value)) throw UserError.InvalidField("Email", "not formated.")
        }
    }

    @JvmInline @Serializable
    value class Password(val value: String){
        init{
            value.maximumLength("Password",20)
            value.cannotByBlank("Password")
        }
    }
}

private fun String.maximumLength(fieldName: String,number: Int){
    if(length>number) throw UserError.InvalidField(fieldName, "the maximum length is $number.")
}

private fun String.cannotByBlank(fieldName: String){
    if(isEmpty()) throw UserError.InvalidField(fieldName, "cannot be blank.")
}