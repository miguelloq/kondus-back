package com.example.core.models

import com.example.core.plugins.getUserId
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.RoutingContext
import io.ktor.server.response.respond

//Inline, reified and noinline is for kotlin shenanigans
suspend inline fun <reified T: Exception> RoutingContext.catchingHttp(
    noinline onCatchT: (suspend RoutingContext.(T) -> Unit)? = null,
    noinline block: suspend RoutingContext.() -> Unit
) = try {
    block()
} catch (err: Exception) {
    val message = err.message
    when (err) {
        is T if(onCatchT != null) -> onCatchT(err)
        is T if(message != null) -> call.respond(HttpStatusCode.BadRequest, message)
        else -> call.respond(HttpStatusCode.InternalServerError)
    }
}

suspend inline fun <reified T: Exception> RoutingContext.catchingHttpAndId(
    noinline onCatchT: (suspend RoutingContext.(T) -> Unit)? = null,
    noinline block: suspend RoutingContext.(CoreUser.Id) -> Unit
){
    catchingHttp<T>(onCatchT){
        val userId = getUserId()
            ?: call.respond(HttpStatusCode.Unauthorized).let{ return@catchingHttp }
        block(CoreUser.Id(userId))
    }
}