package com.example.core.model

data class TokenConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm:String
)




