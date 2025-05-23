package com.example.core.plugins.authentication

data class TokenConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm:String
)