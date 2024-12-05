package com.example.core.presenter.extension

import com.example.core.models.CoreUser
import com.example.core.plugins.getUserId
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.RoutingContext
import io.ktor.server.response.respond

/**
 * Extension function for `RoutingContext` that provides a reusable mechanism to handle domain-specific exceptions
 * and fallback to a general error handling strategy. This helps reduce code duplication when dealing with
 * common error patterns in route handlers.
 *
 * ## Purpose:
 * This function aims to simplify error handling in routing blocks. It allows you to:
 * - Catch and process domain-specific exceptions of type [T] using the optional `onCatchT` handler.
 * - Respond with an HTTP `400 Bad Request` status code and the exception message if [T] is not explicitly handled.
 * - Respond with an HTTP `500 Internal Server Error` for any other unhandled exceptions.
 *
 * ## Parameters:
 * @param T The specific exception type to handle.
 * @param onCatchT A lambda to handle exceptions of type [T] when provided. This is optional.
 *                 If not provided, a `400 Bad Request` response will be sent.
 * @param block The main block of code where the logic for the route is defined.
 *
 * ## Example Usage:
 * Before using this function, route handlers typically required repetitive try-catch blocks:
 * ```kotlin
 * post {
 *     try {
 *         val dto = call.receive<RegisterUserDto>()
 *         registerUserUsecase(dto)
 *         call.respond(HttpStatusCode.NoContent)
 *     } catch (userErr: UserError) {
 *         call.respond(HttpStatusCode.BadRequest, userErr.message)
 *     } catch (_: Exception) {
 *         call.respond(HttpStatusCode.InternalServerError)
 *     }
 * }
 * ```
 * This pattern is common when catching domain-specific exceptions and falling back to `500 Internal Server Error`
 * for unknown errors.
 *
 * To reduce duplication, a dedicated function like this can encapsulate the logic:
 * ```kotlin
 * suspend fun RoutingContext.catchingUserError(block: suspend RoutingContext.() -> Unit) = try {
 *     block()
 * } catch (userErr: UserError) {
 *     call.respond(HttpStatusCode.BadRequest, userErr.message)
 * } catch (_: Exception) {
 *     call.respond(HttpStatusCode.InternalServerError)
 * }
 *
 * post {
 *     catchingUserError {
 *         val dto = call.receive<RegisterUserDto>()
 *         registerUserUsecase(dto)
 *         call.respond(HttpStatusCode.NoContent)
 *     }
 * }
 * ```
 * However, this approach still requires a custom `catchingFooError` function for each domain-specific exception.
 *
 * The `catchingHttp` function generalizes this pattern by allowing any domain-specific exception to be handled
 * with an optional handler, avoiding the need for multiple specialized functions:
 * ```kotlin
 * post {
 *     catchingHttp<UserError>(
 *         onCatchT = { userErr ->
 *             // Custom handling for UserError
 *             call.respond(HttpStatusCode.BadRequest, userErr.message)
 *         }
 *     ) {
 *         val dto = call.receive<RegisterUserDto>()
 *         registerUserUsecase(dto)
 *         call.respond(HttpStatusCode.NoContent)
 *     }
 * }
 * ```
 */
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

/**
 * Extension function for `RoutingContext` that combines the functionality of `catchingHttp` with user authentication.
 *
 * ## Purpose:
 * This function retrieves the authenticated user's ID and ensures the user is authorized before
 * executing the provided `block`. If no user ID is found, it responds with `401 Unauthorized` and skips further execution.
 *
 * For error handling, it relies on the logic defined in `catchingHttp`, allowing the handling of domain-specific exceptions
 * of type [T]. Refer to the `catchingHttp` documentation for details.
 *
 * ## Parameters:
 * @param T The specific exception type to handle.
 * @param onCatchT A lambda to handle exceptions of type [T] (optional).
 * @param block The main block of code, receiving the `CoreUser.Id` of the authenticated user.
 *
 * ## Example Usage:
 * ```kotlin
 * post {
 *     catchingHttpAndId<UserError> { id ->
 *         val dto = call.receive<RegisterUserDto>()
 *         registerUserUsecase(dto, id)
 *         call.respond(HttpStatusCode.NoContent)
 *     }
 * }
 * ```
 */
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