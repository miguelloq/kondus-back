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
import com.example.modules.items.presenter.dto.ItemFinderDto
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.utils.io.readBuffer
import kotlinx.io.readByteArray
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
                val itemId = itemRepository.create(id,item)
                call.respond(HttpStatusCode.Created,itemId)
            }
        }

        post("/images"){
            catchingHttpAndId<ItemError> { id ->
                var itemId: Int? = null
                val images = mutableListOf<ImageDto>()
                call.receiveMultipart().forEachPart { part ->
                    when(part){
                        is PartData.FormItem if(part.name=="itemId") -> itemId = part.value.toIntOrNull()
                        is PartData.FileItem if(part.name=="image") -> {
                            if(!part.isImage()) throw ItemError.InvalidField("Image","is not a image")
                            val fileBytes = part.provider().readBuffer().readByteArray()
                            val fileName =  part.originalFileName
                            val contentType = part.contentType?.toString() ?: "image/jpeg"
                            images.add(ImageDto(
                                name = fileName,
                                bytes = fileBytes,
                                contentType = contentType
                            ))
                        }
                        else -> Unit
                    }
                    part.dispose()
                }
                val itemEntity = itemRepository.validateImages(id,itemId)
                itemRepository.updateImages(itemEntity,images)
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

private fun PartData.FileItem.isImage(): Boolean{
    val contentType = contentType?.withoutParameters()?.toString()
    return when (contentType) {
        "image/png", "image/jpeg", "image/jpg", "image/gif" -> true
        else -> false
    }
}

class ImageDto(
    val name: String?,
    val bytes: ByteArray,
    val contentType: String
)