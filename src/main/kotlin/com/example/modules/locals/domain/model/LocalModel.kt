package com.example.modules.locals.domain.model

import com.example.modules.locals.domain.error.LocalError
import kotlinx.serialization.Serializable

@Serializable
data class LocalModel(
    val name: Name,
    val description: Description,
    val category: Category,
    val address: Address,
){
    @JvmInline @Serializable value class Name(val s: String){
        init {
            val maximumLength = 20
            if(s.isEmpty()) throw LocalError.InvalidField("Name", "cannot be blank.")
            if(s.length>maximumLength) throw LocalError.InvalidField("Name", "the maximum length is $maximumLength.")
        }
    }

    @JvmInline @Serializable value class Description(val s: String){
        init {
            val maximumLength = 50
            if(s.length>maximumLength) throw LocalError.InvalidField("Name", "the maximum length is $maximumLength.")
        }
    }

    @Serializable
    sealed interface Category{
        data object Apartment: Category
        data object Condominium: Category

        companion object{
            fun of(s: String) = when(s.lowercase()){
                "apartment" -> Apartment
                "condominium" -> Condominium
                else -> throw LocalError.InvalidField("Category","type is invalid.")
            }
        }
    }

    @Serializable
    data class Address(
        val street: Street,
        val number: Number,
        val cep: Cep,
    ){
        @JvmInline @Serializable value class Street(val s: String){
            init{
                val maximumLength = 25
                if(s.isEmpty()) throw LocalError.InvalidField("Street", "cannot be blank.")
                if(s.length>maximumLength) throw LocalError.InvalidField("Street", "the maximum length is $maximumLength.")
            }
        }
        @JvmInline @Serializable value class Number(val n: Int){
            init {
                val maxDigits = 9
                if (n <= 0)
                    throw LocalError.InvalidField("Number", "must be greater than zero.")
                if (n.toString().length > maxDigits)
                    throw LocalError.InvalidField("Number", "cannot exceed $maxDigits digits.")
            }
        }
        @JvmInline @Serializable value class Cep(val n: Int){
            init {
                val minDigits = 7
                val maxDigits = 8
                val length = n.toString().length
                if (length < minDigits)
                    throw LocalError.InvalidField("Cep", "must have at least $minDigits digits.")

                if (length > maxDigits)
                    throw LocalError.InvalidField("Cep", "cannot exceed $maxDigits digits.")
            }
        }
    }
}