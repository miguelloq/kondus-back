package com.example.core.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.core.model.TokenConfig
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingContext

fun Application.configureAuthentication(){
    val tokenConfig = getTokenConfig()

    authentication {
        jwt("core-auth"){
            realm = tokenConfig.realm
            verifier(JWT
                .require(Algorithm.HMAC256(tokenConfig.secret))
                .withAudience(tokenConfig.audience)
                .withIssuer(tokenConfig.issuer)
                .build())

            validate { credential ->
                val audienceMatches = credential.payload.audience.contains(tokenConfig.audience)
                return@validate if(audienceMatches) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

fun Application.getTokenConfig() = TokenConfig(
    secret = environment.config.property("jwt.secret").getString(),
    issuer = environment.config.property("jwt.issuer").getString(),
    audience = environment.config.property("jwt.audience").getString(),
    realm = environment.config.property("jwt.realm").getString()
)

fun RoutingContext.getUserId(): Long? =
    try{
        call.principal<JWTPrincipal>()
            ?.getClaim("id",Long::class)
            ?.toLong()
    }catch(_: Exception){
         null
    }



