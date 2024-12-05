package com.example.modules.locals.domain.repository

import com.example.modules.locals.domain.model.LocalModel

interface LocalRepository{
    suspend fun create(model: LocalModel, creatorUserId: Long): Long
    suspend fun get(localId: Long): LocalModel?
}