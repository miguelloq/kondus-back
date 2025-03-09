package com.example.modules.locals.domain.repository

import com.example.core.models.CoreUser
import com.example.modules.locals.domain.model.LocalModel

interface LocalRepository{
    suspend fun create(model: LocalModel): Long
    suspend fun get(localId: Long): LocalModel?
}