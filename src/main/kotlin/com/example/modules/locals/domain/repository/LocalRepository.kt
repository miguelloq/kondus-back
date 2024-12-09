package com.example.modules.locals.domain.repository

import com.example.core.models.CoreUser
import com.example.modules.locals.domain.model.LocalModel

interface LocalRepository{
    suspend fun create(model: LocalModel, creatorUserId: Long): Long
    suspend fun get(localId: Long): LocalModel?
    suspend fun userIsLocalOwner(userId: CoreUser.Id, localId: Long): Boolean
}