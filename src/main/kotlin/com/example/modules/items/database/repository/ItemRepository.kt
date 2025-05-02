package com.example.modules.items.database.repository

import com.example.core.models.CoreUser
import com.example.core.plugins.suspendTransaction
import com.example.modules.items.domain.ItemError
import com.example.modules.items.domain.toItemTypeEnum
import com.example.modules.items.presenter.dto.CategoryDto
import com.example.modules.items.presenter.dto.CreateItemDto
import com.example.modules.items.presenter.dto.ItemDto
import com.example.modules.items.presenter.dto.ItemFinderDto
import com.example.modules.items.presenter.dto.ItemUserDto
import com.example.modules.items.presenter.dto.toCategoryDto
import com.example.modules.items.presenter.dto.toItemDto
import com.example.modules.items.presenter.dto.toItemUserDto
import com.example.modules.locals.data.repository.HouseEntity
import com.example.modules.locals.data.repository.Houses
import com.example.modules.users.data.repository.UserEntity
import com.example.modules.users.data.repository.UserTable
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.*
import java.security.SecureRandom

class ItemRepository(
    private val awsService: AwsService
) {
    private suspend fun CoreUser.Id.toEntity() = suspendTransaction {
        UserEntity.find { UserTable.id eq value.toInt() }.firstOrNull() ?: throw ItemError.NotFound("User")
    }

    suspend fun getItemById(loggedUser: CoreUser.Id, id:Int): ItemUserDto = suspendTransaction {
        val item = ItemEntity.findById(id) ?: throw ItemError.NotFound("Item")
        if(item.user.house.local.id.value != loggedUser.toEntity().house.local.id.value ) throw ItemError.ActionNotPermitted
        item.toItemUserDto()
    }

    suspend fun create(loggedUser: CoreUser.Id, item: CreateItemDto): Int = suspendTransaction {
        val userEntity = loggedUser.toEntity()
        val entity = ItemEntity.new {
            title = item.title
            description = item.description
            type = item.type
            price = item.price?.toBigDecimal()
            quantity = item.quantity
            user = userEntity
            categories = SizedCollection(item.categoriesIds.mapNotNull {
                CategoryEntity.find { Categories.id eq it }.firstOrNull()
            })
        }
        entity.id.value
    }

    suspend fun validateImages(
        loggedUser: CoreUser.Id,
        itemId: Int?
    ): ItemEntity = suspendTransaction{
        val itemEntity = ItemEntity
            .find { Items.id eq (itemId ?: throw ItemError.InvalidField("Item","is obrigatory.")) }
            .firstOrNull() ?: throw ItemError.NotFound("Item")

        if(itemEntity.user.id.value != loggedUser.value.toInt()) throw ItemError.ActionNotPermitted
        itemEntity
    }

    suspend fun updateImages(itemEntity: ItemEntity, images: List<Pair<ByteArray,String?>>) = suspendTransaction{
        images.forEach { (byteArray,name) ->
            val cryptoName = generateSecureRandomString(15) + "-" + name
            val awsS3ImagePath = awsService.uploadS3(cryptoName,byteArray)
            ItemImageEntity.new {
                item = itemEntity
                imagePath = awsS3ImagePath
            }
        }
    }

    suspend fun getCategories(): List<CategoryDto> = suspendTransaction {
        CategoryEntity
            .all()
            .map{ it.toCategoryDto() }
    }

    suspend fun getAllFromUser(loggedUser: CoreUser.Id): List<ItemDto> = suspendTransaction {
        ItemEntity
            .find { Items.user eq loggedUser.value.toInt() }
            .map { it.toItemDto() }
    }

    suspend fun delete(loggedUser: CoreUser.Id, itemId: Int) = suspendTransaction{
        val item = ItemEntity.findById(itemId) ?: throw ItemError.NotFound("Item")
        if(item.user.id.value != loggedUser.value.toInt()) throw ItemError.ActionNotPermitted
        item.delete()
    }

    private suspend fun allItemsFromUserLocal(userId: CoreUser.Id, finder: ItemFinderDto): SizedIterable<ItemEntity> = suspendTransaction{
        val userLocal = userId.toEntity().house.local

        val allHouseIdsFromUserLocal = HouseEntity
            .find { Houses.localId eq userLocal.id.value }
            .map{ it.id.value }

        val allUsersFromUserLocal = UserEntity
            .find { UserTable.houseId inList allHouseIdsFromUserLocal }
            .mapNotNull {
                if(it.id.value == userId.value.toInt()) null
                else it.id.value
            }

        ItemEntity.find {
            Items.user inList allUsersFromUserLocal and
                    (finder.search?.let { Items.title.upperCase().like("%${it.uppercase()}%") } ?: Op.TRUE)
        }
    }

    suspend fun getByFinder(loggedUser: CoreUser.Id, finder: ItemFinderDto): List<ItemUserDto> = suspendTransaction {
        fun `items with types on finder`(itemEntity: ItemEntity): Boolean =
            if(finder.types.isEmpty()) true
            else finder.types
                .map { it.toItemTypeEnum { throw ItemError.InvalidField("Type", "can only be produto or serviço or aluguel") } }
                .any{ it == itemEntity.itemTypeEnum }
        fun `items with categories on finder`(itemEntity: ItemEntity): Boolean{
            if(finder.categoriesIds.isEmpty()) return true
            val categoriesId = itemEntity.categories.map { it.id.value }.toHashSet()
            return finder.categoriesIds.any{ it in categoriesId }
        }

        val allItemsFromUserLocal = allItemsFromUserLocal(loggedUser,finder)
            .filter(::`items with types on finder`)
            .filter(::`items with categories on finder`)

        allItemsFromUserLocal.map { it.toItemUserDto() }
    }
}

private fun generateSecureRandomString(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789&()-_=+"
    val secureRandom = SecureRandom()
    return (1..length)
        .map { chars[secureRandom.nextInt(chars.length)] }
        .joinToString("")
}