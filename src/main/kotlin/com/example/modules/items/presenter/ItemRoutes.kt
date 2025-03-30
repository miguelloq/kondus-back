package com.example.modules.items.presenter

import com.example.core.plugins.authentication.AuthenticationType
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import com.example.core.presenter.extension.catchingHttp
import com.example.core.presenter.extension.catchingHttpAndId
import com.example.modules.items.database.repository.ItemRepository
import com.example.modules.items.domain.ItemError
import com.example.modules.items.presenter.dto.CreateItemDto
import com.example.modules.items.presenter.dto.ItemDto
import com.example.modules.items.presenter.dto.ItemFinderDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.itemRoutes(
    itemRepository: ItemRepository = application.inject<ItemRepository>().value
) = route("/items"){
    authenticate(AuthenticationType.Core.value) {
        get{
            catchingHttpAndId<ItemError> { id ->
                val finder = call.receive<ItemFinderDto>()
                itemRepository
                    .getByFinder(id, finder)
                    .let{ call.respond(it) }
            }
        }

        get("/my"){
            catchingHttpAndId<ItemError> { id ->
                itemRepository
                    .getAllFromUser(id)
                    .let{ call.respond(it) }
            }
        }

        get("/{id}"){
            catchingHttpAndId<ItemError> { id ->
                val itemId = runCatching{
                    call.parameters["id"]!!.toInt()
                }.getOrElse { throw ItemError.InvalidField("ItemId","is not a intenger") }
                itemRepository
                    .getItemById(id,itemId)
                    .let{ call.respond(it) }
            }
        }

        get("/categories"){
            catchingHttp<ItemError> {
                itemRepository
                    .getCategories()
                    .let{ call.respond(it) }
            }
        }

        post{
            catchingHttpAndId<ItemError> { id ->
                val item = call.receive<CreateItemDto>()
                itemRepository.create(id,item)
                call.respond(HttpStatusCode.Created)
            }
        }
        delete("/{id}") {
            catchingHttpAndId<ItemError> { id ->
                val itemId = runCatching{
                    call.parameters["id"]!!.toInt()
                }.getOrElse { throw ItemError.InvalidField("ItemId","is not a intenger") }
                itemRepository.delete(id,itemId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}